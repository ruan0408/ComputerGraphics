#version 130

void main(void) {
	gl_Position=gl_ModelViewProjectionMatrix*gl_Vertex;
    gl_FrontColor = gl_Color;
    gl_BackColor = vec4(0,1,0,1);  
}


