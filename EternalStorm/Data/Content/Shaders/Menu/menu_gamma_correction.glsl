#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D texture;
uniform vec2 resolution;

void main() {
    vec2 uv = gl_FragCoord.xy / resolution;
    vec4 tex = texture2D(texture, uv);

    vec3 color = tex.rgb;

    // Перцептивная яркость
    float luma = dot(color, vec3(0.2126, 0.7152, 0.0722));

    // Возводим яркость в степень 0.8
    float newLuma = pow(luma, 0.95);

    // Масштабируем цвет
    float k = (luma > 0.00001) ? (newLuma / luma) : 1.0;
    vec3 newColor = color * k;

    gl_FragColor = vec4(newColor, tex.a);
}
