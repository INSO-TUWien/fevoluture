package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models;

import com.arangodb.springframework.annotation.Ref;
import org.springframework.data.annotation.Transient;

import java.util.List;

public class FileLogicalCoupling {

    @Ref
    private FileLike root;

    private List<FileCoupling> couplings;

    public FileLike getRoot() {
        return root;
    }

    public FileLogicalCoupling setRoot(FileLike root) {
        this.root = root;
        return this;
    }

    public List<FileCoupling> getCouplings() {
        return couplings;
    }

    public FileLogicalCoupling setCouplings(List<FileCoupling> fileCouplings) {
        this.couplings = fileCouplings;
        return this;
    }

    public static class FileCoupling {

        private FileLike file;
        private int countChangedTogether;
        private int countRoot;
        private int  countFile;
        private double support;
        private double confidence;

        public FileLike getFile() {
            return file;
        }

        public FileCoupling setFile(FileLike file) {
            this.file = file;
            return this;
        }

        public int getCountChangedTogether() {
            return countChangedTogether;
        }

        public FileCoupling setCountChangedTogether(int countChangedTogether) {
            this.countChangedTogether = countChangedTogether;
            return this;
        }

        public int getCountRoot() {
            return countRoot;
        }

        public FileCoupling setCountRoot(int countRoot) {
            this.countRoot = countRoot;
            return this;
        }

        public int getCountFile() {
            return countFile;
        }

        public FileCoupling setCountFile(int countFile) {
            this.countFile = countFile;
            return this;
        }

        public double getSupport() {
            return support;
        }

        public FileCoupling setSupport(double support) {
            this.support = support;
            return this;
        }

        public double getConfidence() {
            return confidence;
        }

        public FileCoupling setConfidence(double confidence) {
            this.confidence = confidence;
            return this;
        }
    }

    // Like file but without Namespace
    public static class FileLike extends File {

        @Transient
        protected Package aPackage1;

        @Transient
        @Override
        public FileLike setaPackage(Package aPackage) {
            return this;
        }

        @Transient
        @Override
        public Package getaPackage() {
            return null;
        }
    }
}
