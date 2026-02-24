#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform vec2 resolution;
uniform float u_frame;
uniform sampler2D plan1;
uniform sampler2D plan2;
uniform sampler2D plan3;
uniform sampler2D plan4;

// Зеркальный тайлинг
vec2 tileUV(vec2 uv) {
    uv = mod(uv, 2.0);
    uv = mix(uv, 2.0 - uv, step(1.0, uv));
    return uv;
}

// Псевдослучайная функция
float rand(vec2 co) {
    return fract(sin(dot(co.xy, vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {
    float mp = 0.5;
    vec2 uv = gl_FragCoord.xy / resolution;

    // --- слабые хаотичные синусоидальные искажения ---
    float wave1 = sin(uv.y * 30.0 + u_frame * 0.008) * 0.008;
    float wave2 = cos(uv.x * 25.0 - u_frame * 0.01) * 0.007;
    float wave3 = sin(uv.x * 15.0 + uv.y * 20.0 + u_frame * 0.005) * 0.009;
    float wave4 = cos(uv.x * 12.0 - uv.y * 18.0 + u_frame * 0.006) * 0.006;

    vec2 uv_distort1 = uv + vec2(wave1 + wave3, wave2 + wave4);
    vec2 uv_distort2 = uv + vec2(wave2 - wave4, wave1 - wave3);
    vec2 uv_distort3 = uv + vec2(-wave1 + wave4, wave3 - wave2);

    // --- слабый хаотичный шум для локальных мелких вихрей ---
    float noise1 = (rand(uv * 150.0 + u_frame * 0.002) - 0.5) * 0.015;
    float noise2 = (rand(uv * 180.0 - u_frame * 0.003) - 0.5) * 0.012;
    float noise3 = (rand(uv * 200.0 + u_frame * 0.004) - 0.5) * 0.013;

    vec2 uv2 = vec2(uv.x * 2.0, uv.y);

    // --- текстуры с хаотичными смещениями ---
    vec3 pc1 = texture2D(plan1, tileUV(uv_distort1 + vec2(u_frame * 0.0005 * mp + noise1, 0.0))).rgb;
    vec3 pc2 = texture2D(plan2, tileUV(uv_distort2 + vec2(u_frame * 0.00063 * mp - noise2, 0.0))).rgb;
    vec3 pc3 = texture2D(plan3, tileUV(uv_distort3 - vec2(u_frame * 0.00071 * mp + noise3, 0.0))).rgb;
    vec3 pc4 = texture2D(plan4, tileUV(uv2 - vec2(u_frame * 0.00076 * mp * 0.5, 0.0))).rgb / 2.0 + 0.5;
    vec3 pc5 = texture2D(plan3, tileUV(vec2(-uv2.x, uv2.y) * vec2(1., 1.4) - vec2(u_frame * 0.0003 * mp, 0.3))).rgb;

    vec3 finalColor = pc1 * pc2 * pc3 * pc4 * pc5;

    gl_FragColor = vec4(finalColor*2.2, 1.0);
}
