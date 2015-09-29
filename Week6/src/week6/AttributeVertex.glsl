#version 130

in vec4 vertexCol;

void main(void) {
	gl_Position=gl_ModelViewProjectionMatrix*gl_Vertex;
    gl_FrontColor = vertexCol;
   
}


