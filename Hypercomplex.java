import java.util.Arrays;

public abstract class Hypercomplex {
    public static final Hypercomplex ZERO = new Scalar(0);

    public static Hypercomplex of(float f) {
        return f == 0 ? ZERO : new Scalar(f);
    }

    public static Hypercomplex of(Hypercomplex a, Hypercomplex b) {
        return b.isZero() ? a : new Composite(a, b);
    }

    public static Hypercomplex of(float a, float b) {
        return of(of(a), of(b));
    }

    public static Hypercomplex of(float... parts) {
        if(parts.length == 0) {
            return ZERO;
        }
        else if(parts.length == 1) {
            return of(parts[0]);
        }
        else if(parts.length == 2) {
            return of(parts[0], parts[1]);
        }
        return of(
                of(Arrays.copyOfRange(parts, 0, parts.length / 2)),
                of(Arrays.copyOfRange(parts, parts.length / 2, parts.length)));
    }

    public abstract int size();

    public abstract float[] flatten();

    public abstract Hypercomplex re();

    public abstract Hypercomplex im();

    public abstract boolean isZero();

    public abstract float norm();

    public abstract Hypercomplex neg();

    public abstract Hypercomplex conj();

    public Hypercomplex inv() {
        float norm = this.norm();
        return this.conj().div(of(norm * norm));
    }

    public Hypercomplex add(float b) {
        return of(re().add(b), im().add(b));
    }

    public Hypercomplex add(Hypercomplex b) {
        return of(re().add(b.re()), im().add(b.im()));
    }

    public Hypercomplex sub(float b) {
        return of(re().sub(b), im().sub(b));
    }

    public Hypercomplex sub(Hypercomplex b) {
        return of(re().sub(b.re()), im().sub(b.im()));
    }

    public Hypercomplex subRight(float a) {
        return of(re().subRight(a), im().subRight(a));
    }

    public Hypercomplex subRight(Hypercomplex a) {
        return of(re().subRight(a), im().subRight(a));
    }

    public Hypercomplex mul(float b) {
        return of(re().mul(b), im().mul(b));
    }

    public Hypercomplex mul(Hypercomplex other) {
        Hypercomplex a = re();
        Hypercomplex b = im();
        Hypercomplex c = other.re();
        Hypercomplex d = other.im();
        return of(a.mul(c).sub(d.conj().mul(b)), d.mul(a).add(b.mul(c.conj())));
    }

    public Hypercomplex mulRight(Hypercomplex a) {
        return a.mul(this);
    }

    public Hypercomplex div(float b) {
        return of(re().div(b), im().div(b));
    }

    public Hypercomplex div(Hypercomplex b) {
        return this.mul(b.inv());
    }

    public Hypercomplex divRight(float a) {
        return of(re().divRight(a), im().divRight(a));
    }

    public Hypercomplex divRight(Hypercomplex a) {
        return a.div(this);
    }

    public static class Scalar extends Hypercomplex {
        private final float f;

        public Scalar(float f) {
            this.f = f;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public float[] flatten() {
            return new float[] {f};
        }

        @Override
        public Hypercomplex re() {
            return this;
        }

        @Override
        public Hypercomplex im() {
            return ZERO;
        }

        @Override
        public boolean isZero() {
            return f == 0;
        }

        @Override
        public float norm() {
            return f;
        }

        @Override
        public Hypercomplex neg() {
            return of(-f);
        }

        @Override
        public Hypercomplex conj() {
            return this;
        }

        @Override
        public Hypercomplex add(float b) {
            return of(f + b);
        }

        @Override
        public Hypercomplex add(Hypercomplex b) {
            return b.add(f);
        }

        @Override
        public Hypercomplex sub(float b) {
            return of(f - b);
        }

        @Override
        public Hypercomplex sub(Hypercomplex b) {
            return b.subRight(f);
        }

        @Override
        public Hypercomplex subRight(float a) {
            return of(a - f);
        }

        @Override
        public Hypercomplex subRight(Hypercomplex a) {
            return a.sub(f);
        }

        @Override
        public Hypercomplex mul(float b) {
            return of(f * b);
        }

        @Override
        public Hypercomplex mul(Hypercomplex b) {
            return b.mul(f);
        }

        @Override
        public Hypercomplex mulRight(Hypercomplex a) {
            return a.mul(f);
        }

        @Override
        public Hypercomplex div(float b) {
            return of(f / b);
        }

        @Override
        public Hypercomplex div(Hypercomplex b) {
            return b.divRight(f);
        }

        @Override
        public Hypercomplex divRight(float a) {
            return of(a / f);
        }

        @Override
        public Hypercomplex divRight(Hypercomplex a) {
            return a.div(f);
        }
    }

    public static class Composite extends Hypercomplex {
        private final Hypercomplex a;
        private final Hypercomplex b;

        private Composite(Hypercomplex a, Hypercomplex b) {
            this.a = a;
            this.b = b;
        }

        public int halfSize() {
            return Math.max(a.size(), b.size());
        }

        @Override
        public int size() {
            return halfSize() * 2;
        }

        @Override
        public float[] flatten() {
            int halfSize = halfSize();
            float[] arr = new float[halfSize * 2];
            float[] aParts = a.flatten();
            float[] bParts = b.flatten();
            System.arraycopy(aParts, 0, arr, 0, aParts.length);
            System.arraycopy(bParts, 0, arr, halfSize, bParts.length);
            return arr;
        }

        @Override
        public Hypercomplex re() {
            return a;
        }

        @Override
        public Hypercomplex im() {
            return b;
        }

        @Override
        public boolean isZero() {
            return a.isZero() && b.isZero();
        }

        @Override
        public float norm() {
            float agg = 0;
            for(float f : flatten()) {
                agg += f * f;
            }
            return (float)Math.sqrt(agg);
        }

        @Override
        public Hypercomplex neg() {
            return of(a.neg(), b.neg());
        }

        @Override
        public Hypercomplex conj() {
            return of(a, b.neg());
        }
    }
}