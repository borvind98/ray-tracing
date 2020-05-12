package com.company;

public abstract class Material {

    boolean scatter(Ray rIn, HitRecord rec, Vec3 attenuation, Ray scattered){
        return true;
    }
}


class Lambertian extends Material{

    Vec3 albedo;

    Lambertian(Vec3 col){
        this.albedo = col;
    }

    boolean scatter(Ray rIn, HitRecord rec, Vec3 attenuation, Ray scattered){
        Vec3 scatterDirection = Vec3.vec_plus(rec.normal, Vec3.randomUnitVector());
        scattered.set(rec.p, scatterDirection);
        attenuation.set(albedo);
        return true;
    }

}

class Metal extends Material{

    Vec3 albedo;
    float fuzz;

    Metal(Vec3 col, float f){
        this.albedo = col;
        if(f < 1){
            this.fuzz = f;
        }
        else{
            this.fuzz = 1;
        }

    }

    boolean scatter(Ray rIn, HitRecord rec, Vec3 attenuation, Ray scattered){
        Vec3 reflected = Vec3.reflect(Vec3.unit_vector(rIn.direction()), rec.normal);
        scattered.set(rec.p, Vec3.vec_plus(reflected, Vec3.vec_mul_t(Vec3.randomUnitVector(), fuzz)));
        attenuation.set(albedo);
        return (Vec3.dot(scattered.direction(), rec.normal) > 0);
    }

}

class Dielectrics extends Material{
    float refIdx;

    Dielectrics(float ri){
        this.refIdx = ri;

    }

    boolean scatter(Ray rIn, HitRecord rec, Vec3 attenuation, Ray scattered){

        attenuation.set(new Vec3(1.0f, 1.0f, 1.0f));
        double etaiOverEtat = refIdx;
        if(rec.frontFace){
            etaiOverEtat = 1.0f / refIdx;
        }

        Vec3 unitDirection = Vec3.unit_vector(rIn.direction());
        double cosTheta = Math.min(Vec3.dot(Vec3.vec_minus(new Vec3(0,0,0), unitDirection), rec.normal), 1.0f);
        double sinTheta = Math.sqrt(1.0f - cosTheta*cosTheta);
        if(etaiOverEtat * sinTheta > 1.0){
            Vec3 reflected = Vec3.reflect(unitDirection, rec.normal);
            scattered.set(rec.p, reflected);
            return true;
        }
        Vec3 refracted = Vec3.refract(unitDirection, rec.normal, etaiOverEtat);
        scattered.set(rec.p, refracted);
        return true;
    }
}
