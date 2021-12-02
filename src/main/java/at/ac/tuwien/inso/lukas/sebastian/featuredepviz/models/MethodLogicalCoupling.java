package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models;

import com.arangodb.springframework.annotation.Ref;

import java.util.List;

public class MethodLogicalCoupling {

    @Ref
    private Method root;

    private List<MethodCoupling> couplings;

    public Method getRoot() {
        return root;
    }

    public MethodLogicalCoupling setRoot(Method root) {
        this.root = root;
        return this;
    }

    public List<MethodCoupling> getCouplings() {
        return couplings;
    }

    public MethodLogicalCoupling setCouplings(List<MethodCoupling> couplings) {
        this.couplings = couplings;
        return this;
    }

    public static class MethodCoupling {

        private Method method;
        private int countChangedTogether;
        private int countRoot;
        private int countMethod;
        private double support;
        private double confidence;

        public Method getMethod() {
            return method;
        }

        public MethodCoupling setMethod(Method method) {
            this.method = method;
            return this;
        }

        public int getCountChangedTogether() {
            return countChangedTogether;
        }

        public MethodCoupling setCountChangedTogether(int countChangedTogether) {
            this.countChangedTogether = countChangedTogether;
            return this;
        }

        public int getCountRoot() {
            return countRoot;
        }

        public MethodCoupling setCountRoot(int countRoot) {
            this.countRoot = countRoot;
            return this;
        }

        public int getCountMethod() {
            return countMethod;
        }

        public MethodCoupling setCountMethod(int countMethod) {
            this.countMethod = countMethod;
            return this;
        }

        public double getSupport() {
            return support;
        }

        public MethodCoupling setSupport(double support) {
            this.support = support;
            return this;
        }

        public double getConfidence() {
            return confidence;
        }

        public MethodCoupling setConfidence(double confidence) {
            this.confidence = confidence;
            return this;
        }
    }
}
