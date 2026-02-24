#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform vec2 resolution;
uniform float u_frame;
uniform float u_waterLevel;
uniform sampler2D u_texture;

uniform sampler2D plan1;
uniform sampler2D plan2;
uniform sampler2D plan3;
uniform sampler2D plan4;

vec2 tileUV(vec2 uv) {
    return mod(uv, 1.0);
}

void main() {
    vec2 uv = gl_FragCoord.xy / resolution.xy;

    float t = u_frame * 0.00005;
    float dist = (u_waterLevel - uv.y) / u_waterLevel;
    float invDist = 1.0 / (dist + 0.1);

    // Шум для отражения и тумана
    vec2 noiseUV1 = uv * 3.5 + vec2(t * 0.15, -t * 0.1);
    vec2 noiseUV2 = uv * 5.0 - vec2(t * 0.12, t * 0.08);

    float noise1 = texture2D(plan1, tileUV(noiseUV1)).r;
    float noise2 = texture2D(plan1, tileUV(noiseUV2)).g;

    // Если выше водной линии - туман и свечение
    if(uv.y > u_waterLevel) {
        float heightAboveWater = (uv.y - u_waterLevel) / (1.0 - u_waterLevel);

        // Сложный многослойный туман - растягиваем по Y, тайлим по X
        vec2 fogUV1 = vec2(uv.x * 2.5, heightAboveWater * 1.5) + vec2(t * 0.08, t * 0.05);
        vec2 fogUV2 = vec2(uv.x * 3.8, heightAboveWater * 2.2) - vec2(t * 0.06, -t * 0.04);
        vec2 fogUV3 = vec2(uv.x * 5.2, heightAboveWater * 1.8) + vec2(t * 0.1, t * 0.07);
        vec2 fogUV4 = vec2(uv.x * 4.1, heightAboveWater * 2.5) - vec2(t * 0.09, -t * 0.06);
        vec2 fogUV5 = vec2(uv.x * 6.3, heightAboveWater * 2.0) + vec2(t * 0.11, t * 0.08);

        // Тайлим только X координату
        float fog1 = texture2D(plan1, vec2(tileUV(vec2(fogUV1.x, 0.0)).x, fogUV1.y)).r;
        float fog2 = texture2D(plan2, vec2(tileUV(vec2(fogUV2.x, 0.0)).x, fogUV2.y)).g;
        float fog3 = texture2D(plan3, vec2(tileUV(vec2(fogUV3.x, 0.0)).x, fogUV3.y)).b;
        float fog4 = texture2D(plan4, vec2(tileUV(vec2(fogUV4.x, 0.0)).x, fogUV4.y)).r;
        float fog5 = texture2D(plan1, vec2(tileUV(vec2(fogUV5.x, 0.0)).x, fogUV5.y)).g;

        // Волновой туман
        float fogWave1 = sin(uv.x * 8.0 + t * 1.2 + fog1 * 3.0) * cos(heightAboveWater * 15.0 - t * 0.9);
        float fogWave2 = sin(uv.x * 12.0 - t * 1.5 + fog2 * 4.0) * cos(heightAboveWater * 18.0 + t * 1.1);
        float fogWave3 = cos(uv.x * 15.0 + t * 1.8 + fog3 * 3.5) * sin(heightAboveWater * 20.0 - t * 1.3);
        float fogWave4 = sin(uv.x * 10.0 + t * 1.4 + fog4 * 3.8) * cos(heightAboveWater * 16.0 + t * 1.0);

        float fogDensity = fog1 * 0.25 + fog2 * 0.22 + fog3 * 0.2 + fog4 * 0.18 + fog5 * 0.15;
        fogDensity += (fogWave1 + fogWave2 + fogWave3 + fogWave4) * 0.08;
        fogDensity = pow(fogDensity, 1.5);

        // Туман сильнее у воды и слабее выше - плавный переход
        float fogFade = pow(1.0 - heightAboveWater, 2.5) * 0.85;
        fogDensity *= fogFade;

        // Свечение - зеленое прям над водой (в радиусе ~2%), темно-красное выше
        float distanceFromWater = heightAboveWater * (1.0 - u_waterLevel); // в единицах экрана

        // Зеленое свечение только в зоне ~2% от линии воды
        float glowGreen = smoothstep(0.03, 0.0, distanceFromWater) * smoothstep(0.0, 0.01, distanceFromWater);

        // Темно-красное свечение везде выше, но слабее у самой воды
        float glowRed = smoothstep(0.0, 0.05, heightAboveWater) * (1.0 - glowGreen * 0.5) * 0.3;

        // Пульсация свечения
        float glowPulse1 = sin(t * 2.5 + uv.x * 5.0) * cos(t * 1.8 + heightAboveWater * 10.0);
        float glowPulse2 = sin(t * 3.2 - uv.x * 6.0) * cos(t * 2.1 - heightAboveWater * 12.0);
        float glowPulse3 = cos(t * 2.8 + uv.x * 4.5) * sin(t * 2.4 - heightAboveWater * 14.0);
        float glowPulse = (glowPulse1 + glowPulse2 + glowPulse3) * 0.12 + 0.88;

        // Неравномерное свечение с шумом - растягиваем по Y
        vec2 glowNoiseUV1 = vec2(uv.x * 4.0, heightAboveWater * 3.0) + vec2(t * 0.12, t * 0.08);
        vec2 glowNoiseUV2 = vec2(uv.x * 5.5, heightAboveWater * 4.0) - vec2(t * 0.1, -t * 0.07);
        vec2 glowNoiseUV3 = vec2(uv.x * 3.2, heightAboveWater * 2.5) + vec2(t * 0.14, t * 0.09);

        float glowNoise1 = texture2D(plan4, vec2(tileUV(vec2(glowNoiseUV1.x * 0.3, 0.0)).x, glowNoiseUV1.y * 0.3)).r;
        float glowNoise2 = texture2D(plan2, vec2(tileUV(vec2(glowNoiseUV2.x, 0.0)).x, glowNoiseUV2.y * 0.3)).g;
        float glowNoise3 = texture2D(plan3, vec2(tileUV(vec2(glowNoiseUV3.x, 0.0)).x, glowNoiseUV3.y * 0.3)).b;
        float glowNoise = (glowNoise1 + glowNoise2 + glowNoise3) * 0.33;

        // Яркое зеленое прям над водой
        vec3 glowColor = vec3(glowGreen * 0.3, glowGreen * 0.65, glowGreen * 0.25) * (1.0 - heightAboveWater * 2.);
        // Темно-красное выше
        glowColor += vec3(glowRed * 0.35, glowRed * 0.1, glowRed * 0.05);
        glowColor *= glowPulse * glowNoise * 0.5 * (1.0 - heightAboveWater * 1.);

        // Цвет тумана - плавный градиент от зеленоватого внизу к красноватому выше
        vec3 fogColor = mix(vec3(0.12, 0.22, 0.1),  // зеленоватый внизу
        vec3(0.3, 0.14, 0.08),   // красноватый выше
        smoothstep(0.0, 0.3, heightAboveWater));

        // Получаем оригинальную текстуру с растяжением по Y
        vec2 baseUV = vec2(uv.x, u_waterLevel + heightAboveWater * (1.0 - u_waterLevel));
        vec3 baseColor = texture2D(u_texture, baseUV).rgb;

        // Смешиваем туман и свечение
        vec3 finalColor = mix(baseColor, fogColor, fogDensity * 0.5);
        finalColor += glowColor;

        gl_FragColor = vec4(finalColor, 1.0);
        return;
    }

    // Вода - все ниже
    vec2 pUV = vec2((uv.x - 0.5) * invDist, invDist + t * 0.5);

    // Крупные медленные волны - усилены
    float bigWave1 = sin(pUV.x * 3.2 + t * 0.7) * cos(pUV.y * 2.8 - t * 0.53);
    float bigWave2 = sin(pUV.x * 2.7 - t * 0.63) * sin(pUV.y * 1.9 + t * 0.41);
    float bigWave3 = cos(pUV.x * 2.1 + t * 0.55) * sin(pUV.y * 3.1 - t * 0.47);

    // Средние волны - усилены
    float medWave1 = sin(pUV.x * 8.3 + t * 1.47) * cos(pUV.y * 6.2 - t * 1.23);
    float medWave2 = cos(pUV.x * 7.4 - t * 1.35) * sin(pUV.y * 5.8 + t * 0.91);
    float medWave3 = sin(pUV.x * 6.7 + t * 1.18) * cos(pUV.y * 7.1 - t * 1.41);

    // Мелкие быстрые волны - усилены
    float smallWave1 = sin(pUV.x * 21.3 + t * 3.1) * cos(pUV.y * 18.7 - t * 2.6);
    float smallWave2 = sin(pUV.x * 26.2 - t * 3.7) * cos(pUV.y * 22.8 + t * 3.3);
    float smallWave3 = cos(pUV.x * 31.5 + t * 4.2) * sin(pUV.y * 28.9 - t * 3.9);
    float smallWave4 = sin(pUV.x * 19.7 - t * 2.8) * sin(pUV.y * 24.3 + t * 3.5);

    // Комбинированные волновые паттерны - значительно усилены
    float wavePattern = bigWave1 * 0.35 + bigWave2 * 0.32 + bigWave3 * 0.28 +
        medWave1 * 0.24 + medWave2 * 0.22 + medWave3 * 0.2 +
        smallWave1 * 0.16 + smallWave2 * 0.14 + smallWave3 * 0.15 + smallWave4 * 0.13;

    // Усиленное отражение с блюром и множеством эффектов
    vec2 ssr_uv = vec2(uv.x, u_waterLevel + (u_waterLevel - uv.y));

    // Усиленные искажения от состояния воды
    float reflectBigWave = bigWave1 * 0.008 + bigWave2 * 0.007 + bigWave3 * 0.0075;
    reflectBigWave *= (1.0 - dist * 0.6);

    float reflectMedWave = medWave1 * 0.006 + medWave2 * 0.005 + medWave3 * 0.0055;
    reflectMedWave *= (1.0 - dist * 0.4);

    float reflectSmallWave = (smallWave1 + smallWave2 + smallWave3 + smallWave4) * 0.004;
    reflectSmallWave *= (1.0 - dist * 0.2);

    // Усиленные дополнительные волновые эффекты
    float reflectWave1 = sin(uv.x * 22.0 + t * 2.5 + bigWave1 * 6.0) * cos(uv.y * 18.0 - t * 2.0 + medWave1 * 5.0);
    float reflectWave2 = sin(uv.x * 32.0 - t * 3.2 + medWave2 * 7.0) * cos(uv.y * 28.0 + t * 2.6 + smallWave1 * 4.5);
    float reflectWave3 = cos(uv.x * 42.0 + t * 3.8 + wavePattern * 8.0) * sin(uv.y * 38.0 - t * 3.2 + bigWave2 * 6.5);
    float reflectWave4 = sin(uv.x * 28.0 - t * 2.8 + smallWave3 * 5.5) * cos(uv.y * 24.0 + t * 2.2 + medWave3 * 6.0);

    float combinedReflectWave = (reflectWave1 + reflectWave2 + reflectWave3 + reflectWave4) * 0.0035 * (1.0 - dist * 0.5);

    float reflectNoise = (noise1 - 0.5) * 0.006 * (1.0 - dist * 0.8);

    ssr_uv += vec2(reflectBigWave + reflectMedWave + reflectSmallWave + combinedReflectWave + reflectNoise, (reflectBigWave + reflectMedWave) * 0.6);

    ssr_uv = clamp(ssr_uv, 0.0, 1.0);

    // Блюр отражения - несколько сэмплов
    vec3 reflection = vec3(0.0);
    float blurAmount = 0.002 * (1.0 - dist * 0.7);

    reflection += texture2D(u_texture, ssr_uv).rgb * 0.25;
    reflection += texture2D(u_texture, ssr_uv + vec2(blurAmount, 0.0)).rgb * 0.15;
    reflection += texture2D(u_texture, ssr_uv - vec2(blurAmount, 0.0)).rgb * 0.15;
    reflection += texture2D(u_texture, ssr_uv + vec2(0.0, blurAmount)).rgb * 0.15;
    reflection += texture2D(u_texture, ssr_uv - vec2(0.0, blurAmount)).rgb * 0.15;
    reflection += texture2D(u_texture, ssr_uv + vec2(blurAmount * 0.7, blurAmount * 0.7)).rgb * 0.075;
    reflection += texture2D(u_texture, ssr_uv - vec2(blurAmount * 0.7, blurAmount * 0.7)).rgb * 0.075;

    float fade = pow(1.0 - dist, 1.15);
    float fadeMod = sin(wavePattern * 4.0 + t) * 0.2 + 0.8;
    fade *= fadeMod * 1.5;

    reflection *= fade;

    // Хроматическая аберрация
    vec2 aberrationOffset = vec2(0.002 * (1.0 - dist) * (1.0 + wavePattern * 0.8), 0.0);
    vec3 reflectionChroma;
    reflectionChroma.r = texture2D(u_texture, clamp(ssr_uv + aberrationOffset, 0.0, 1.0)).r;
    reflectionChroma.g = reflection.g;
    reflectionChroma.b = texture2D(u_texture, clamp(ssr_uv - aberrationOffset, 0.0, 1.0)).b;

    reflection = mix(reflection, reflectionChroma, 0.4);

    // Усиленное мерцание
    float shimmer1 = sin(uv.x * 45.0 + t * 4.5 + bigWave1 * 8.0) * cos(uv.y * 40.0 - t * 3.8 + medWave1 * 7.0);
    float shimmer2 = sin(uv.x * 58.0 - t * 5.2 + medWave2 * 9.0) * cos(uv.y * 52.0 + t * 4.5 + smallWave2 * 6.5);
    float shimmer3 = cos(uv.x * 68.0 + t * 5.8 + wavePattern * 10.0) * sin(uv.y * 62.0 - t * 5.0 + smallWave3 * 8.0);

    float shimmer = (shimmer1 + shimmer2 + shimmer3) * 0.33;
    shimmer = pow((shimmer * 0.5 + 0.5), 2.5) * 0.25 * (1.0 - dist * 0.6);
    reflection *= (1.0 + shimmer);

    // Рябь на отражении
    float ripple = sin(uv.x * 75.0 + t * 6.5 + smallWave1 * 12.0) * cos(uv.y * 70.0 - t * 6.0 + smallWave4 * 11.0);
    ripple = pow(max(0.0, ripple), 3.5) * 0.18 * (1.0 - dist * 0.75);
    reflection *= (1.0 + ripple);

    // Текстурные слои воды - усиленные искажения
    vec2 parallax1 = pUV * 1.2 + vec2(t * 0.08, t * 0.05) + wavePattern * 0.35;
    vec2 parallax2 = pUV * 0.9 - vec2(t * 0.06, t * 0.04) + bigWave1 * 0.32;
    vec2 parallax3 = pUV * 1.6 + vec2(t * 0.1, -t * 0.07) + medWave1 * 0.38;
    vec2 parallax4 = pUV * 0.7 - vec2(t * 0.05, t * 0.03) + smallWave1 * 0.25;

    vec2 noiseUV3 = pUV * 7.0 + vec2(-t * 0.18, t * 0.14);
    vec2 noiseUV4 = pUV * 4.2 + vec2(t * 0.21, t * 0.16);

    float noise3 = texture2D(plan1, tileUV(noiseUV3)).b;
    float noise4 = texture2D(plan1, tileUV(noiseUV4)).r;

    vec3 layer1 = texture2D(plan2, tileUV(parallax1 + noise1 * 0.1)).rgb;
    vec3 layer2 = texture2D(plan3, tileUV(parallax2 + noise2 * 0.08)).rgb;
    vec3 layer3 = texture2D(plan4, tileUV(parallax3 + noise3 * 0.12)).rgb;
    vec3 layer4 = texture2D(plan2, tileUV(parallax4 + wavePattern * 0.06)).rgb;

    vec3 water_texture = layer1 * 0.3 + layer2 * 0.28 + layer3 * 0.25 + layer4 * 0.17;

    // Вода ярче
    float colorMod = sin(t * 0.5 + dist * 3.0) * 0.1 + 0.9;
    water_texture *= vec3(0.28, 0.42, 0.32) * colorMod;

    float depthWave = (bigWave1 + medWave1 * 0.5) * 0.1 * dist;
    water_texture += depthWave * vec3(0.5, 1.0, 0.6);

    // Пенистые блики
    float foam = pow(max(0.0, wavePattern), 11.0) * 0.04;
    foam += pow(max(0.0, smallWave1 + smallWave2 + smallWave3 + smallWave4), 12.0) * 0.03;
    foam *= (1.0 - dist * 0.5);

    vec2 foamUV = pUV * 3.2 + vec2(t * 0.3, -t * 0.2);
    vec3 foamTexture = texture2D(plan4, tileUV(foamUV)).rgb;
    float foamFilter = dot(foamTexture, vec3(0.33));

    foamFilter = min(foamFilter, 3.0);
    foam = sqrt(foam + 1) - 1;

    water_texture += vec3(foam * 0.12, foam * 0.18, foam * 0.14) * foamFilter;

    // Горизонтальные пятна
    float horizBand1 = sin(pUV.y * 8.0 + t * 1.2 + noise1 * 5.0) * cos(pUV.x * 3.0 + noise2 * 4.0);
    float horizBand2 = sin(pUV.y * 12.0 - t * 1.5 + noise3 * 6.0) * cos(pUV.x * 4.5 + noise4 * 3.5);
    float horizBand3 = cos(pUV.y * 15.0 + t * 1.8 + noise2 * 5.5) * sin(pUV.x * 5.0 + noise1 * 4.5);

    float horizPattern = pow(max(0.0, horizBand1), 6.0) * 0.04;
    horizPattern += pow(max(0.0, horizBand2), 7.0) * 0.03;
    horizPattern += pow(max(0.0, horizBand3), 8.0) * 0.025;

    vec2 horizUV = vec2(pUV.x * 2.5, pUV.y * 4.0) + vec2(t * 0.15, t * 0.1);
    vec3 horizTexture = texture2D(plan3, tileUV(horizUV)).rgb;
    float horizFilter = dot(horizTexture, vec3(0.33));

    // Ограничение яркости зеленых пятен
    vec3 horizColor = vec3(0.1, 0.2, 0.12) * horizPattern * horizFilter;
    horizColor = min(horizColor, vec3(0.15, 0.25, 0.18)); // Максимальная яркость
    water_texture += horizColor;

    // Каустики
    float caustic1 = sin(pUV.x * 22.5 + t * 2.8 + noise1 * 4.2) * cos(pUV.y * 19.3 - t * 2.1 + noise2 * 3.8);
    float caustic2 = sin(pUV.x * 27.8 - t * 3.5 + noise3 * 4.5) * cos(pUV.y * 24.1 + t * 3.2 + noise4 * 4.1);
    float caustic3 = cos(pUV.x * 20.3 + t * 2.6 + noise2 * 3.9) * sin(pUV.y * 23.4 - t * 3.3 + noise1 * 4.3);
    float caustic4 = sin(pUV.x * 31.2 - t * 4.1 + noise4 * 5.1) * sin(pUV.y * 26.7 + t * 3.8 + noise3 * 4.6);
    float caustic5 = cos(pUV.x * 25.6 + t * 3.4 + noise3 * 4.7) * cos(pUV.y * 21.8 - t * 2.9 + noise2 * 3.5);

    float caustics = pow(max(0.0, caustic1 * caustic2), 8.0) * 0.018;
    caustics += pow(max(0.0, caustic3 * caustic4), 9.0) * 0.015;
    caustics += pow(max(0.0, caustic2 * caustic5), 10.0) * 0.012;
    caustics += pow(max(0.0, caustic1 * caustic5), 8.5) * 0.014;
    caustics *= (1.0 - dist * 0.65);

    vec2 causticUV = pUV * 2.8 + vec2(t * 0.25, -t * 0.18) + wavePattern * 0.2;
    vec3 causticTexture = texture2D(plan3, tileUV(causticUV)).rgb;
    float causticFilter = dot(causticTexture, vec3(0.33));

    // Ограничение яркости каустик
    vec3 causticColor = vec3(0.08, 0.18, 0.1) * caustics * causticFilter;
    causticColor = min(causticColor, vec3(0.12, 0.22, 0.15)); // Максимальная яркость
    water_texture += causticColor;

    float depthDarkening = pow(dist, 1.0);
    water_texture = mix(water_texture, vec3(0.05, 0.1, 0.06), depthDarkening * 0.48);

    water_texture = pow(water_texture, vec3(1.1));

    vec3 finalColor = mix(water_texture, reflection, 0.5);

    gl_FragColor = vec4(finalColor, 1.0);
}