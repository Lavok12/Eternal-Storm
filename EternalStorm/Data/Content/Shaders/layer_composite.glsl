#ifdef GL_ES
precision mediump float;
#endif

#define MAX_LAYERS 16

uniform sampler2D u_layers[MAX_LAYERS];
uniform vec4 u_layerParams[MAX_LAYERS];
uniform float u_layerAlpha[MAX_LAYERS];
uniform vec2 u_layerResolution[MAX_LAYERS];
uniform int u_layerCount;
uniform vec2 u_resolution;

void main() {
    vec4 finalColor = vec4(0.0);

    for(int i = 0; i < MAX_LAYERS; i++) {
        if(i >= u_layerCount)
            break;

        vec2 offset = u_layerParams[i].xy;
        vec2 scale = u_layerParams[i].zw;
        vec2 lRes = u_layerResolution[i];

        vec2 uv = (gl_FragCoord.xy - offset) / (lRes * scale);

        if(uv.x < 0.0 || uv.y < 0.0 || uv.x > 1.0 || uv.y > 1.0) {
            continue;
        }
        vec4 texColor = texture2D(u_layers[i], uv);
        float srcAlpha = texColor.a * u_layerAlpha[i];
        float outAlpha = srcAlpha + finalColor.a * (1.0 - srcAlpha);

        if(outAlpha > 0.0) {
            finalColor.rgb = (texColor.rgb * srcAlpha + finalColor.rgb * finalColor.a * (1.0 - srcAlpha)) / outAlpha;
        }

        finalColor.a = outAlpha;
    }

    gl_FragColor = finalColor;
}