#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_base;
uniform sampler2D u_overlay;

uniform vec2 u_baseResolution;
uniform vec2 u_overlayResolution;

uniform vec2 u_overlayOffset;
uniform vec2 u_overlayScale;
uniform float u_overlayAlpha;

void main() {
    vec2 fragPos = gl_FragCoord.xy;

    vec2 baseUV = fragPos / u_baseResolution;
    vec4 baseColor = texture2D(u_base, baseUV);

    vec4 overlayColor = texture2D(u_overlay, baseUV);


    float alpha = u_overlayAlpha * overlayColor.a;
    gl_FragColor = baseColor * alpha + overlayColor * (1.0 - alpha);
}
