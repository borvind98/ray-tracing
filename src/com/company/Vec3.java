package com.company;
public class Vec3 {

    double e0, e1, e2;

    Vec3(double e0, double e1, double e2){
        this.e0 = e0;
        this.e1 = e1;
        this.e2 = e2;
    }

    double x() { return e0;}
    double y() { return e1;}
    double z() { return e2;}

    double length(){
        return Math.sqrt(e0*e0 + e1*e1 + e2*e2);
    }

    double squared_length(){
        return e0*e0 + e1*e1 + e2*e2;
    }

    void make_unit_vector(){
        double k = (1.0f /Math.sqrt(e0*e0 + e1*e1 + e2*e2));
        e0 *= k; e1 *= k; e2 *= k;
    }

    void set(Vec3 v){
        this.e0 = v.e0;
        this.e1 = v.e1;
        this.e2 = v.e2;
    }

    Vec3 vecPlus(Vec3 v2){
        return new Vec3(this.e0 + v2.e0, this.e1 + v2.e1, this.e2 + v2.e2);
    }

    Vec3 vecMinus(Vec3 v2){
        return new Vec3(this.e0 - v2.e0, this.e1 - v2.e1, this.e2 - v2.e2);
    }

    Vec3 vecMul(Vec3 v2){
        return new Vec3(this.e0 * v2.e0, this.e1 * v2.e1, this.e2 * v2.e2);
    }

    Vec3 vecDiv(Vec3 v2){
        return new Vec3(this.e0 / v2.e0, this.e1 / v2.e1, this.e2 / v2.e2);
    }

    Vec3 vecPlusT(double t){
        return new Vec3(this.e0 + t, this.e1 + t, this.e2 + t);
    }

    Vec3 vecMinusT(double t){
        return new Vec3(this.e0 - t, this.e1 - t, this.e2 - t);
    }

    Vec3 vecMulT(double t){
        return new Vec3(this.e0 * t, this.e1 * t, this.e2 * t);
    }

    Vec3 vecDivT(double t){
        return new Vec3(this.e0 / t, this.e1 / t, this.e2 / t);
    }

    Vec3 cross(Vec3 v2){
        return new Vec3( (this.e1*v2.e2 - this.e2*v2.e1),
                (-(this.e0*v2.e2 - this.e2*v2.e0)),
                (this.e0*v2.e1 - this.e1*v2.e0));
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

    static Vec3 vec_plus_t(Vec3 v, double t){
        return new Vec3(v.e0 + t, v.e1 + t, v.e2 + t);
    }

    static Vec3 vec_minus_t(Vec3 v, double t){
        return new Vec3(v.e0 - t, v.e1 - t, v.e2 - t);
    }

    static Vec3 vec_mul_t(Vec3 v, double t){
        return new Vec3(v.e0 * t, v.e1 * t, v.e2 * t);
    }

    static Vec3 vec_div_t(Vec3 v, double t){
        return new Vec3(v.e0 / t, v.e1 / t, v.e2 / t);
    }

    double dot(Vec3 v2){
        return this.e0 * v2.e0 + this.e1 * v2.e1 + this.e2 * v2.e2;
    }

    static Vec3 cross(Vec3 v1, Vec3 v2){
        return new Vec3((v1.e1*v2.e2 - v1.e2*v2.e1),
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

    Vec3 mul_t(double t){
        this.e0 *= t;
        this.e1 *= t;
        this.e2 *= t;
        return this;
    }

    Vec3 div_t(double t){
        double k = 1.0/t;

        this.e0 *= k;
        this.e1 *= k;
        this.e2 *= k;
        return this;
    }

    Vec3 mulT(double t){
        this.e0 *= t;
        this.e1 *= t;
        this.e2 *= t;
        return this;
    }

    Vec3 divT(double t){
        double k = 1.0/t;
        return new Vec3(this.e0 * k, this.e1 * k, this.e2 * k);
    }

    static Vec3 unit_vector(Vec3 v){
        return v.divT(v.length());
    }

    static Vec3 randomUnitVector(){
        double a =  RandomNumGen.random_double_within_interval(0, 2*Math.PI);
        double z =  RandomNumGen.random_double_within_interval(-1, 1);
        double r =  Math.sqrt(1 - z*z);
        return new Vec3((r*Math.cos(a)), (r*Math.sin(a)), z);
    }

    static Vec3 randomInUnitDisk(){
        Vec3 p = new Vec3(RandomNumGen.random_double_within_interval(-1, 1), RandomNumGen.random_double_within_interval(-1, 1), 0);
        while(p.squared_length() >= 1){
            p = new Vec3(RandomNumGen.random_double_within_interval(-1, 1), RandomNumGen.random_double_within_interval(-1, 1), 0);
        }
        return p;
    }

    static Vec3 makeRandomVec(){
        return new Vec3(RandomNumGen.random_double(), RandomNumGen.random_double(), RandomNumGen.random_double());
    }

    static Vec3 makeRandomVecWithMinMax(double min, double max){
        return new Vec3(RandomNumGen.random_double_within_interval(min, max), RandomNumGen.random_double_within_interval(min, max), RandomNumGen.random_double_within_interval(min, max));
    }

    static Vec3 random_in_unit_sphere(){
        Vec3 p = Vec3.makeRandomVecWithMinMax(-1, 1);
        while(p.squared_length() >= 1){
            p = Vec3.makeRandomVecWithMinMax(-1, 1);
        }
        return p;
    }

    static Vec3 reflect(Vec3 v, Vec3 n){
        return v.vecMinus(n.vecMulT(2*v.dot(n)));
    }

    static Vec3 refract(Vec3 uv, Vec3 n, double etaiOverEtat){
        double cosTheta = new Vec3(0,0,0).vecMinus(uv).dot(n);
        Vec3 rOutParallel = uv.vecPlus(n.vecMulT(cosTheta)).vecMulT(etaiOverEtat);
        Vec3 rOutPerp = n.vecMulT(-Math.sqrt(1.0 - rOutParallel.squared_length()));
        return vec_plus(rOutParallel, rOutPerp);
    }


}
