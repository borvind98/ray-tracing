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
        scattered.set(rec.p, scatterDirection, rIn.time());
        attenuation.set(albedo);
        return true;
    }

}

class Metal extends Material{

    Vec3 albedo;
    double fuzz;

    Metal(Vec3 col, double f){
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
        return (scattered.direction().dot(rec.normal) > 0);
    }

}

class Dielectrics extends Material{
    double refIdx;

    Dielectrics(double ri){
        this.refIdx = ri;

    }

    boolean scatter(Ray rIn, HitRecord rec, Vec3 attenuation, Ray scattered){

        attenuation.set(new Vec3(1.0, 1.0, 1.0));
        double etaiOverEtat = refIdx;
        if(rec.frontFace){
            etaiOverEtat = 1.0 / refIdx;
        }

        Vec3 unitDirection = Vec3.unit_vector(rIn.direction());
        double cosTheta = Math.min(new Vec3(0,0,0).vecMinus(unitDirection).dot(rec.normal), 1.0);
        double sinTheta = Math.sqrt(1.0f - cosTheta*cosTheta);
        if(etaiOverEtat * sinTheta > 1.0){
            Vec3 reflected = Vec3.reflect(unitDirection, rec.normal);
            scattered.set(rec.p, reflected);
            return true;
        }
        double reflectProb = schlick(cosTheta, etaiOverEtat);
        if(RandomNumGen.randomDouble() < reflectProb){
            Vec3 reflected = Vec3.reflect(unitDirection, rec.normal);
            scattered.set(rec.p, reflected);
            return true;
        }

        Vec3 refracted = Vec3.refract(unitDirection, rec.normal, etaiOverEtat);
        scattered.set(rec.p, refracted);
        return true;
    }

    double schlick(double cosine, double refIdx){
        double r0 = (1-refIdx) / (1+refIdx);
        r0 = r0*r0;
        return r0 + (1-r0)*(Math.pow(1-cosine,5));
    }
}
