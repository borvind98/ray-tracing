package com.company;
public class Vec3 {

    float e0, e1, e2;

    Vec3(float e0, float e1, float e2){
        this.e0 = e0;
        this.e1 = e1;
        this.e2 = e2;
    }

    float x() { return e0;}
    float y() { return e1;}
    float z() { return e2;}
    float r() { return e0;}
    float g() { return e1;}
    float b() { return e2;}

    float length(){
        return (float) Math.sqrt(e0*e0 + e1*e1 + e2*e2);
    }

    float squared_length(){
        return e0*e0 + e1*e1 + e2*e2;
    }

    void make_unit_vector(){
        float k = (float) (1.0f /Math.sqrt(e0*e0 + e1*e1 + e2*e2));
        e0 *= k; e1 *= k; e2 *= k;
    }

    void set(Vec3 v){
        this.e0 = v.e0;
        this.e1 = v.e1;
        this.e2 = v.e2;
    }

    static Vec3 vec_plus(Vec3 v1, Vec3 v2){
        return new Vec3(v1.e0 + v2.e0, v1.e1 + v2.e1, v1.e2 + v2.e2);
    }

    static Vec3 vec_minus(Vec3 v1, Vec3 v2){
        return new Vec3(v1.e0 - v2.e0, v1.e1 - v2.e1, v1.e2 - v2.e2);
    }

    static Vec3 vec_mul(Vec3 v1, Vec3 v2){
        return new Vec3(v1.e0 * v2.e0, v1.e1 * v2.e1, v1.e2 * v2.e2);
    }

    static Vec3 vec_div(Vec3 v1, Vec3 v2){
        return new Vec3(v1.e0 / v2.e0, v1.e1 / v2.e1, v1.e2 / v2.e2);
    }

    static Vec3 vec_plus_t(Vec3 v, float t){
        return new Vec3(v.e0 + t, v.e1 + t, v.e2 + t);
    }

    static Vec3 vec_minus_t(Vec3 v, float t){
        return new Vec3(v.e0 - t, v.e1 - t, v.e2 - t);
    }

    static Vec3 vec_mul_t(Vec3 v, float t){
        return new Vec3(v.e0 * t, v.e1 * t, v.e2 * t);
    }

    static Vec3 vec_div_t(Vec3 v, float t){
        return new Vec3(v.e0 / t, v.e1 / t, v.e2 / t);
    }

    static float dot(Vec3 v1, Vec3 v2){
        return v1.e0 * v2.e0 + v1.e1 * v2.e1 + v1.e2 * v2.e2;
    }

    Vec3 cross(Vec3 v1, Vec3 v2){
        return new Vec3( (v1.e1*v2.e2 - v1.e2*v2.e1),
                        (-(v1.e0*v2.e2 - v1.e2*v2.e0)),
                        (v1.e0*v2.e1 - v1.e1*v2.e0));
    }

    Vec3 add(Vec3 v){
        e0 += v.e0;
        e1 += v.e1;
        e2 += v.e2;
        return this;
    }

    Vec3 sub(Vec3 v){
        e0 -= v.e0;
        e1 -= v.e1;
        e2 -= v.e2;
        return this;
    }

    Vec3 mul(Vec3 v){
        this.e0 *= v.e0;
        this.e1 *= v.e1;
        this.e2 *= v.e2;
        return this;
    }

    Vec3 div(Vec3 v){
        this.e0 /= v.e0;
        this.e1 /= v.e1;
        this.e2 /= v.e2;
        return this;
    }

    Vec3 mul_t(float t){
        this.e0 *= t;
        this.e1 *= t;
        this.e2 *= t;
        return this;
    }

    Vec3 div_t(float t){
        float k = 1.0f/t;

        this.e0 *= k;
        this.e1 *= k;
        this.e2 *= k;
        return this;
    }

    static Vec3 unit_vector(Vec3 v){
        return v.div_t(v.length());
    }

    static Vec3 randomUnitVector(){
        double a =  RandomNumGen.random_double_within_interval(0, 2*Math.PI);
        double z =  RandomNumGen.random_double_within_interval(-1, 1);
        double r =  Math.sqrt(1 - z*z);
        return new Vec3((float) (r*Math.cos(a)), (float) (r*Math.sin(a)), (float) z);
    }

    static Vec3 makeRandomVec(){
        return new Vec3((float)RandomNumGen.random_double(), (float)RandomNumGen.random_double(), (float)RandomNumGen.random_double());
    }

    static Vec3 makeRandomVecWithMinMax(double min, double max){
        return new Vec3((float)RandomNumGen.random_double_within_interval(min, max), (float)RandomNumGen.random_double_within_interval(min, max), (float)RandomNumGen.random_double_within_interval(min, max));
    }

    static Vec3 random_in_unit_sphere(){
        Vec3 p = Vec3.makeRandomVecWithMinMax(-1, 1);
        while(p.squared_length() >= 1){
            p = Vec3.makeRandomVecWithMinMax(-1, 1);
        }
        return p;
    }

    static Vec3 reflect(Vec3 v, Vec3 n){
        return Vec3.vec_minus(v, Vec3.vec_mul_t(Vec3.vec_mul_t(n, Vec3.dot(v,n)),2));
    }

    static Vec3 refract(Vec3 uv, Vec3 n, double etaiOverEtat){
        float cosTheta = dot(new Vec3(0,0,0).sub(uv), n);
        Vec3.vec_mul_t(n, cosTheta);
        Vec3 rOutParallel = Vec3.vec_mul_t(Vec3.vec_plus(uv, Vec3.vec_mul_t(n, cosTheta)), (float) etaiOverEtat);
        Vec3 rOutPerp = Vec3.vec_mul_t(n, (float) - Math.sqrt(1.0 - rOutParallel.squared_length()));
        return vec_plus(rOutParallel, rOutPerp);
    }


}
