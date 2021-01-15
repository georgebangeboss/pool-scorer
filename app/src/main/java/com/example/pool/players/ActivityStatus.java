package com.example.pool.players;

public enum ActivityStatus {
    ACTIVE {
        @Override
        public boolean canPlay() {
            return true;
        }
    },INACTIVE {
        @Override
        public boolean canPlay() {

            return false;
        }
    };
    public abstract boolean canPlay();
}
