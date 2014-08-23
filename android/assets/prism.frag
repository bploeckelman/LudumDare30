#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float u_scale;
uniform vec2 u_dir;

void main()
{
  vec2 nor_dir = normalize(u_dir);
  float colorR = texture2D(u_texture, v_texCoords + (u_scale * nor_dir)).r/3.0;
  vec2 colorY = texture2D(u_texture, v_texCoords + ((u_scale * .66) * nor_dir)).rg/3.0;
  float colorG = texture2D(u_texture, v_texCoords + ((u_scale * .33) * nor_dir)).g/3.0;
  vec2 colorC = texture2D(u_texture, v_texCoords - ((u_scale * .33) * nor_dir)).gb/3.0;
  float colorB = texture2D(u_texture, v_texCoords - ((u_scale * .66) * nor_dir)).b/3.0;
  vec2 colorP = texture2D(u_texture, v_texCoords - (u_scale * nor_dir)).rb/3.0;

  float minY = max(colorY.x, colorY.y);
  float minC = max(colorC.x, colorC.y);
  float minP = max(colorP.x, colorP.y);

  float alpha = max(colorR, max(minY, max(colorG, max(minC, max(colorB, minP)))));
  gl_FragColor = vec4(colorR + minY + minP, colorG + minY + minC, colorB + minC + minP, alpha * 3.0);
}
