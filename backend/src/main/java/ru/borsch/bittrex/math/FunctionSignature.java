package ru.borsch.bittrex.math;

public enum FunctionSignature {
    Up {
        @Override
        public FunctionSignature switchDirection(Double delta) {
            return (delta < 0)? UpDown: Up;
        }

        @Override
        public FunctionSignature switchDirection(FunctionSignature function) {
            return (function.equals(Down))? UpDown: Up;
        }

        @Override
        public String getImageFileName() {
            return "img/up-type.png";
        }
    },
    UpDownUp {
        @Override
        public FunctionSignature switchDirection(Double delta) {
            return UpDownUp;
        }
        @Override
        public FunctionSignature switchDirection(FunctionSignature function) {
            return UpDownUp;
        }
        @Override
        public String getImageFileName() {
            return "img/up-down-up-type.png";
        }
    },
    UpDown {
        @Override
        public FunctionSignature switchDirection(Double delta) {
            return (delta < 0)? UpDown: UpDownUp;
        }
        @Override
        public FunctionSignature switchDirection(FunctionSignature function) {
            return (function.equals(Down))? UpDown: UpDownUp;
        }
        @Override
        public String getImageFileName() {
            return "img/up-down-type.png";
        }
    },
    Down {
        @Override
        public FunctionSignature switchDirection(Double delta) {
            return (delta < 0)? Down: DownUp;
        }
        @Override
        public FunctionSignature switchDirection(FunctionSignature function) {
            return (function.equals(Down))? Down: DownUp;
        }
        @Override
        public String getImageFileName() {
            return "img/down-type.png";
        }
    },
    DownUpDown {
        @Override
        public FunctionSignature switchDirection(Double delta) {
            return DownUpDown;
        }
        @Override
        public FunctionSignature switchDirection(FunctionSignature function) {
            return DownUpDown;
        }
        @Override
        public String getImageFileName() {
            return "img/down-up-down-type.png";
        }
    },
    DownUp {
        @Override
        public FunctionSignature switchDirection(Double delta) {
            return (delta < 0)? DownUpDown: DownUp;
        }
        @Override
        public FunctionSignature switchDirection(FunctionSignature function) {
            return (function.equals(Down))? DownUpDown : DownUp;
        }
        @Override
        public String getImageFileName() {
            return "img/down-up-type.png";
        }
    },
    Base {
        @Override
        public FunctionSignature switchDirection(Double delta) {
            return (delta != 0)?((delta < 0)? Down : Up): Base;
        }
        @Override
        public FunctionSignature switchDirection(FunctionSignature function) {
            return (!function.equals(Base))?((function.equals(Down))? Down : Up): Base;
        }
        @Override
        public String getImageFileName() {
            return "img/base-type.png";
        }
    };

    public abstract FunctionSignature switchDirection(Double delta);
    public abstract FunctionSignature switchDirection(FunctionSignature function);
    public abstract String getImageFileName();
    public String getImageUrl(String baseUrl){
        if (baseUrl.endsWith("/")){
            return String.format("%s%s",baseUrl,getImageFileName());
        }
        return String.format("%s/%s",baseUrl, getImageFileName());
    };
}
