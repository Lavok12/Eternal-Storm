#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D smoke1;
uniform sampler2D smoke2;
uniform sampler2D smoke3;

uniform vec2 resolution;
uniform float time;

uniform vec3 hazeColor;
uniform float hazeStrength;

uniform vec2 parallax;

void main() {
    vec2 uv = gl_FragCoord.xy / resolution;

    // --- Слой 1 (дальний) ---
    vec2 uv1 = fract(uv + parallax * 0.1 + vec2(time * 0.001, 0.0));
    float a1 = texture2D(smoke1, uv1).r;

    // --- Слой 2 (средний) ---
    vec2 uv2 = fract(uv + parallax * 0.25 + vec2(time * 0.002, time * 0.002));
    float a2 = texture2D(smoke2, uv2).r;

    // --- Слой 3 (ближний) ---
    vec2 uv3 = fract(uv + parallax * 0.4 + vec2(time * 0.004, time * 0.003));
    float a3 = texture2D(smoke3, uv3).r;

    float hazeAlpha = a1 * 0.4 + a2 * 0.35 + a3 * 0.25;
    hazeAlpha *= hazeStrength;

    gl_FragColor = vec4(hazeColor, hazeAlpha);
}
