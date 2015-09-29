#version 130

in vec4 colorAgain;
uniform vec4 color;
attribute vec4 otherColor;
out vec4 backColor;

void main(void) {


	gl_Position=gl_ModelViewProjectionMatrix*gl_Vertex;
/*	gl_FrontColor = gl_Color; */
   /*  gl_FrontColor = color; */
   /* gl_FrontColor = otherColor; */
   gl_FrontColor = colorAgain; 
   backColor = color;
}


