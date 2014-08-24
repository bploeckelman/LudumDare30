#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float u_time;

void main()
{


  vec2 cPos = -1.0 + 2.0 * v_texCoords;
  float cLength = length(cPos);

  vec2 uv = v_texCoords.xy+(cPos/cLength)*cos(cLength*12.0-u_time*4.0)*0.02;
  vec4 col = texture2D(u_texture,uv);

  gl_FragColor = vec4(col.xyz, col.a * .4);
}
