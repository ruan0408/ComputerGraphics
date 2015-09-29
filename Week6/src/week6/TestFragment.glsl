#version 130


void main (void) {	
    
     gl_FragColor = gl_Color;
 
     mat4 m = mat4(1,2,3,4,
                   5,6,7,8,
                   9,10,11,
                   12,13,14,15,16);
 	 if(abs(m[0][1] - 2.0) < 0.00001){
 	    gl_FragColor = vec4(0,1,0,1);
 	 } else {
 	
	    gl_FragColor = vec4(0.5,0.5,0.5,1);
     }  		
}

