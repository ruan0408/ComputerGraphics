#version 130

in vec2 texCoordV;

uniform sampler2D texUnit;



void main (void) {	
   
	gl_FragColor = texture(texUnit,texCoordV);
    //gl_FragColor = texture(texUnit,texCoordV) * gl_Color;
}

