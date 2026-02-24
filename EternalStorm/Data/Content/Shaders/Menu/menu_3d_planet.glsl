#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform sampler2D u_texture;
uniform sampler2D ringMap;
uniform vec2 resolution;
uniform vec3 rotation;

vec3 rotateVec(vec3 v, vec3 axis, float angle) {
    float c = cos(angle);
    float s = sin(angle);
    return v * c + cross(axis, v) * s + axis * dot(axis, v) * (1.0 - c);
}

void main() {
    vec2 uv = (gl_FragCoord.xy / resolution) * 2.0 - 1.0;
    float mp = 3.3;
    uv *= mp;

    float r2 = dot(uv, uv);
    float r = sqrt(r2);

    // --- ПЛАНЕТА ---
    bool hitPlanet = r <= 1.0;
    float zPlanet = -10000.0;
    vec3 planetColor = vec3(0.0);
    float planetAlpha = 0.0;

    if(hitPlanet) {
        float z = sqrt(1.0 - r2);
        zPlanet = z;

        vec3 pos = vec3(uv, z);
        vec3 xAxis = vec3(1.0, 0.0, 0.0);
        vec3 yAxis = vec3(0.0, 1.0, 0.0);
        vec3 zAxis = vec3(0.0, 0.0, 1.0);

        pos = rotateVec(pos, xAxis, rotation.x);
        pos = rotateVec(pos, yAxis, rotation.y);
        pos = rotateVec(pos, zAxis, rotation.z);

        float u = fract(0.5 + atan(pos.z, pos.x) / (2.0 * 3.1415926));
        float v = 0.5 - asin(pos.y) / 3.1415926;

        planetColor = texture2D(u_texture, vec2(u, v)).rgb;

        float edge = pow(1.0 - r, 1.0);
        float centerBoost = 1.0 + 0.2 * (1.0 - r);
        planetColor *= edge * centerBoost;
        planetColor *= 1.1;
        planetColor += vec3(0.02, 0.0, 0.0);

        planetAlpha = 1.0;
    }

    // --- КОЛЬЦА ---
    vec3 ringNormal = vec3(0.0, 1.0, 0.0);
    vec3 xAxis = vec3(1.0, 0.0, 0.0);
    vec3 yAxis = vec3(0.0, 1.0, 0.0);
    vec3 zAxis = vec3(0.0, 0.0, 1.0);

    ringNormal = rotateVec(ringNormal, xAxis, rotation.x);
    ringNormal = rotateVec(ringNormal, yAxis, rotation.y);
    ringNormal = rotateVec(ringNormal, zAxis, rotation.z);

    float zRing = -(uv.x * ringNormal.x + uv.y * ringNormal.y) / (ringNormal.z + 0.00001);
    vec3 ringPos3D = vec3(uv, zRing);
    float ringDist = length(ringPos3D);

    bool hitRing = (ringDist > 1.2 && ringDist < 2.2);

    vec3 ringNearColor = vec3(0.2, 0.02, 0.02);
    vec3 ringFarColor = vec3(0.06, 0.01, 0.01);
    float ringAlpha = 0.0;
    vec3 ringColor = ringNearColor;

    if(hitRing) {
        float ringU = (ringDist - 1.2) / (2.2 - 1.2);
        ringAlpha = texture2D(ringMap, vec2(ringU, 0.5)).x;

        float edgeFalloff = smoothstep(1.2, 1.25, ringDist) * (1.0 - smoothstep(2.15, 2.2, ringDist));
        ringAlpha *= edgeFalloff;

        ringColor = mix(ringNearColor, ringFarColor, ringU);

        float lighting = 0.5 + 0.5 * cos(atan(uv.y, uv.x) + rotation.y);
        ringColor *= 0.5 + 0.5 * lighting;

        if(hitPlanet && zRing < zPlanet) {
            ringAlpha *= 0.3; // задний слой
        } else {
            ringAlpha = clamp(ringAlpha * 1.5, 0.0, 1.0); // передний слой
        }
    }

    // --- Слойность ---
    // 1) задний слой колец
    vec3 backRingColor = vec3(0.0);
    float backRingAlpha = 0.0;
    if(hitRing && zRing < zPlanet) {
        backRingColor = ringColor;
        backRingAlpha = ringAlpha;
    }

    // 2) атмосфера за планетой, но над задним кольцом
    vec3 atmosphereColor = vec3(0.3, 0.05, 0.025);
    float atmosphereAlpha = 0.0;
    float atmosphereRadius = 1.15;
    if(r > 1.0 && r <= atmosphereRadius) {
        atmosphereAlpha = smoothstep(atmosphereRadius, 1.0, r) * 0.4;
    }

    vec3 color = planetColor;
    float alpha = planetAlpha;

    // Добавляем задний слой колец
    color = mix(color, backRingColor, backRingAlpha);
    alpha = max(alpha, backRingAlpha);

    // Добавляем атмосферу (только если не перекрывает передний план)
    color = mix(color, atmosphereColor, atmosphereAlpha * (1.0 - alpha));
    alpha = max(alpha, atmosphereAlpha);

    // 3) передний слой колец
    vec3 frontRingColor = vec3(0.0);
    float frontRingAlpha = 0.0;
    if(hitRing && zRing >= zPlanet) {
        frontRingColor = ringColor;
        frontRingAlpha = ringAlpha;
    }
    color = mix(color, frontRingColor, frontRingAlpha);
    alpha = max(alpha, frontRingAlpha);

    // --- Слабый общий туман ---
    float fog = exp(-0.2 * r2);
    color = mix(vec3(0.05, 0.02, 0.01), color, fog);

    gl_FragColor = vec4(color, alpha);
}
