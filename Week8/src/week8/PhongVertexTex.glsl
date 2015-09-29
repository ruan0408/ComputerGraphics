#version 130

out vec3 N; 
out vec4 v; 

out vec2 texCoordV;

void main (void) {	
    v = gl_ModelViewMatrix * gl_Vertex;
    N = vec3(normalize(gl_NormalMatrix * normalize(gl_Normal)));
    
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	texCoordV= vec2(gl_MultiTexCoord0);
	
}

