uniform int numLights;
uniform vec3 lightPositions[256];
uniform vec3 lightValues[256];

varying vec4 diffuses[256];
varying vec3 normal,lightDirs[256],halfVectors[256];
varying float distances[256];

void main()
{
    vec3 n,halfV,viewV,ldir;
    float NdotL,NdotHV;
    vec4 color = vec4(.1,.1,.1,1);

    float att;
    float constantAttenuation=1.0;
    float linearAttenuation=0.22;
    float quadraticAttenuation=0.20; 

    /* a fragment shader can't write a varying variable, hence we need
    a new variable to store the normalized interpolated normal */

    n = normalize(normal);

    /* compute the dot product between normal and normalized lightdir */

    for(int i = 0; i < numLights; i++)
    {
        NdotL = max(dot(n,normalize(lightDirs[i])),0.0);
        if (NdotL > 0.0) {

                att = 1.0 / (constantAttenuation +
                                linearAttenuation * distances[i] +
                                quadraticAttenuation * distances[i] * distances[i]);

                color += att * (diffuses[i] * NdotL);
                halfV = normalize(halfVectors[i]);

                NdotHV = max(dot(n,halfV),0.0);
                color += att * gl_FrontMaterial.specular * pow(NdotHV,gl_FrontMaterial.shininess);
        }
    }
    gl_FragColor = color;
}