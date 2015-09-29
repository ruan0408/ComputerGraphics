#version 130



in vec3 N;
in vec4 v;

/* We are only taking into consideration light0 and assuming it is a point light */
void main (void) {	
   vec4 ambient, globalAmbient;
    
    /* Compute the ambient and globalAmbient terms */
	ambient =  gl_LightSource[0].ambient * gl_FrontMaterial.ambient;
	globalAmbient = gl_LightModel.ambient * gl_FrontMaterial.ambient;

	
	/* Diffuse calculations */

	vec3 normal, lightDir; 
	
	vec4 diffuse;
	float NdotL;
	
	/* normal has been interpolated and may no longer be unit length so we need to normalise*/
	normal = normalize(N);
	
	
	/* normalize the light's direction. */
	lightDir = normalize(vec3(gl_LightSource[0].position - v));
    NdotL = max(dot(normal, lightDir), 0.0); 
    /* Compute the diffuse term */
     diffuse = NdotL * gl_FrontMaterial.diffuse * gl_LightSource[0].diffuse; 

    vec4 specular = vec4(0.0,0.0,0.0,1);
    float NdotHV;
    float NdotR;
    vec3 dirToView = normalize(vec3(-v));
    
    vec3 R = normalize(reflect(-lightDir,normal)); 
    vec3 H =  normalize(lightDir+dirToView); 
   
    /* compute the specular term if NdotL is  larger than zero */
    
	if (NdotL > 0.0) {
		NdotR = max(dot(R,dirToView ),0.0);
		
		//Can use the halfVector instead of the reflection vector if you wish 
		NdotHV = max(dot(normal, H),0.0);
		
		specular = gl_FrontMaterial.specular * gl_LightSource[0].specular * pow(NdotHV,gl_FrontMaterial.shininess);
	    
	}
	
	specular = clamp(specular,0,1);

    gl_FragColor = gl_FrontMaterial.emission + globalAmbient + ambient + diffuse + specular;
	
   
	
}

