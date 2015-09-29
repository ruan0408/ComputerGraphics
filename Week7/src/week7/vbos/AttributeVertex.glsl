#version 130

in vec4 vertexCol;
in vec4 vertexPos;

void main(void) {
	gl_Position=gl_ModelViewProjectionMatrix*vertexPos;
    gl_FrontColor = vertexCol;  
}


