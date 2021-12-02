package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models;

import com.arangodb.springframework.annotation.Field;
import com.arangodb.springframework.annotation.Ref;

import java.util.List;

public class PackageLogicalCoupling {

    @Ref
    private Package root;

    private List<PackageCoupling> couplings;

    public Package getRoot() {
        return root;
    }

    public PackageLogicalCoupling setRoot(Package root) {
        this.root = root;
        return this;
    }

    public List<PackageCoupling> getCouplings() {
        return couplings;
    }

    public PackageLogicalCoupling setCouplings(List<PackageCoupling> packageCouplings) {
        this.couplings = packageCouplings;
        return this;
    }

    public static class PackageCoupling {

        @Field("package")
        private Package aPackage;
        private int countChangedTogether;
        private int countRoot;
        private int countPackage;
        private double support;
        private double confidence;


        public Package getPackage() {
            return aPackage;
        }

        public PackageCoupling setPackage(Package aPackage) {
            this.aPackage = aPackage;
            return this;
        }

        public int getCountChangedTogether() {
            return countChangedTogether;
        }

        public PackageCoupling setCountChangedTogether(int countChangedTogether) {
            this.countChangedTogether = countChangedTogether;
            return this;
        }

        public int getCountRoot() {
            return countRoot;
        }

        public PackageCoupling setCountRoot(int countRoot) {
            this.countRoot = countRoot;
            return this;
        }

        public int getCountPackage() {
            return countPackage;
        }

        public PackageCoupling setCountPackage(int countPackage) {
            this.countPackage = countPackage;
            return this;
        }

        public double getSupport() {
            return support;
        }

        public PackageCoupling setSupport(double support) {
            this.support = support;
            return this;
        }

        public double getConfidence() {
            return confidence;
        }

        public PackageCoupling setConfidence(double confidence) {
            this.confidence = confidence;
            return this;
        }
    }
}
