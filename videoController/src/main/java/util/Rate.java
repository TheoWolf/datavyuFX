package util;

public enum Rate {
    X0(0) {
        @Override
        public Rate next() {
            return X1;
        }

        @Override
        public Rate previous() {
            return this;
        }
    },
    X1D4(1 / 4F) {
        @Override
        public Rate next() {
            return X1D2;
        }

        @Override
        public Rate previous() {
            return this;
        }
    },
    X1D2(1 / 2F) {
        @Override
        public Rate next() {
            return X1;
        }

        @Override
        public Rate previous() {
            return X1D4;
        }
    },
    X1(1F) {
        @Override
        public Rate next() {
            return X2;
        }

        @Override
        public Rate previous() {
            return X1D2;
        }
    },
    X2(2F) {
        @Override
        public Rate next() {
            return X4;
        }

        @Override
        public Rate previous() {
            return X1;
        }
    },
    X4(4F) {
        @Override
        public Rate next() { return this; }

        @Override
        public Rate previous() {
            return X2;
        }
    };

    /**  */
    private float rate;

    Rate(float rate) { this.rate = rate; }

    /**
     * The Rate Value
     *
     * @return value off the rate
     */
    public float getValue() { return this.rate; }

    /**
     * The Play Rate
     *
     * @return X1 Rate
     */
    public static Rate playRate() { return X1; }

    /**
     * The stop Rate (X0)
     *
     * @return X0 Rate
     */
    public static Rate stopRate() { return X0; }

    /**
     * Next Rate
     *
     * @return next Rate
     */
    public abstract Rate next();

    /**
     * Previous Rate
     *
     * @return previous Rate
     */
    public abstract Rate previous();
}
