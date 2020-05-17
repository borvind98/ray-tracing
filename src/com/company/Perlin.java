package com.company;

public class Perlin {

    Vec3[] ranvec;
    int pointCount = 256;
    int[] permX, permY, permZ;

    Perlin(){
        this.ranvec = new Vec3[pointCount];
        for (int i = 0; i < pointCount; i++) {
            ranvec[i] = Vec3.unitVector(Vec3.makeRandomVec(-1, 1));
        }

        this.permX = perlinGeneratePerm();
        this.permY = perlinGeneratePerm();
        this.permZ = perlinGeneratePerm();

    }


    double noise(Vec3 point){
        double u = point.x() - Math.floor(point.x());
        double v = point.y() - Math.floor(point.y());
        double w = point.z() - Math.floor(point.z());

        int i = (int)Math.floor(point.x());
        int j = (int)Math.floor(point.y());
        int k = (int)Math.floor(point.z());
        Vec3[][][] c = new Vec3[2][2][2];

        for (int di = 0; di < 2; di++) {
            for (int dj = 0; dj < 2; dj++) {
                for (int dk = 0; dk < 2; dk++) {
                    c[di][dj][dk] = ranvec[
                            permX[(i+di) & 255]^
                            permY[(j+dj) & 255]^
                            permZ[(k+dk) & 255]];
                }
            }
        }

        return perlinInterp(c, u, v, w);
    }

    int[] perlinGeneratePerm(){
        int[] p = new int[pointCount];
        for (int i = 0; i < pointCount; i++) {
            p[i] = i;
        }
        permute(p, pointCount);

        return p;
    }

    void permute(int[] p, int n){
        for (int i = n-1; i > 0; i--) {
            int target = RandomNumGen.randomInt(0, i);
            int temp = p[i];
            p[i] = p[target];
            p[target] = temp;
        }
    }

    double perlinInterp(Vec3[][][] c, double u, double v, double w){
        double uu = u*u*(3-2*u);
        double vv = v*v*(3-2*v);
        double ww = w*w*(3-2*w);
        double accum = 0.0;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    Vec3 weightV = new Vec3(u-i, v-j, w-k);
                    accum += (i*uu + (1-i)*(1-uu))*
                            (j*vv + (1-j)*(1-vv))*
                            (k*ww + (1-k)*(1-ww))* weightV.dot(c[i][j][k]);
                }
            }
        }
        return accum;
    }

    double turbulence(Vec3 point){
        double accum = 0.0;
        Vec3 tempPoint = point;
        double weight = 1.0;
        int depth = 7;

        for (int i = 0; i < depth; i++) {
            accum += weight*noise(tempPoint);
            weight *= 0.5;
            tempPoint = tempPoint.vecMulT(2);
        }
        return Math.abs(accum);
    }

}
