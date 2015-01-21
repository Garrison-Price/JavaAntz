uniform int numLights;
uniform vec3 lightPositions[256];
uniform vec3 lightValues[256];

varying vec4 diffuses[256];
varying vec3 normal,lightDirs[256],halfVectors[256];
varying float distances[256];

void main() 
{
    vec4 ecPos;
    vec3 aux;
    normal = normalize(gl_NormalMatrix * gl_Normal);

    /* these are the new lines of code to compute the light's direction */
    ecPos = gl_ModelViewMatrix * gl_Vertex;

    for(int i = 0; i < numLights; i++)
    {
        aux = vec3(lightPositions[i]-ecPos);
        lightDirs[i] = normalize(aux);
        distances[i] = length(aux);
        halfVectors[i] = normalize(normalize(lightPositions[i]) + vec3(0, 0, 1));

        /* Compute the diffuse, ambient and globalAmbient terms */
        diffuses[i] = gl_FrontMaterial.diffuse * vec4(lightValues[i],1);
    }
    gl_Position = ftransform();
}