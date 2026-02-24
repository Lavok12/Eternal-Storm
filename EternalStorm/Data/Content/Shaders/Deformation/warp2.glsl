#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform vec2 resolution;

uniform int u_pointCount;
uniform vec2 u_points[16];     // в пикселях
uniform vec2 u_offsets[16];    // в пикселях
uniform float u_radius[16];    // радиус в пикселях

void main() {
    // нормализованные координаты с Y+ вверх
        vec2 uv = gl_FragCoord.xy / resolution;
        uv.y = 1.0 - uv.y;

    vec2 warpedUV = uv;

    for (int i = 0; i < 16; i++) {
        if (i >= u_pointCount) break;

        // нормализуем точки и смещения
        vec2 p = u_points[i] / resolution;
        p.y = 1.0 - p.y; // переворот Y

        vec2 offset = u_offsets[i] / resolution;

        // нормализуем радиус относительно ширины
        float r = u_radius[i] / resolution.x;

        // влияние точки на UV
        float d = distance(uv, p);
        float w = exp(- (d * d) / (r * r));

        warpedUV += offset * w;
    }

    // обращаем Y обратно для текстуры
    vec2 texUV = warpedUV;
    texUV.y = texUV.y;

    vec4 color = texture2D(u_texture, texUV);
    gl_FragColor = color;
}
