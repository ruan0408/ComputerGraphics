#version 130


out vec2 texCoordV;

void main (void) {	
   
    
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	
	gl_FrontColor = gl_Color;
	
	texCoordV= vec2(gl_MultiTexCoord0); //will be interpolated.
}

