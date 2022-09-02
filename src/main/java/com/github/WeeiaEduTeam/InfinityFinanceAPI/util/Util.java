package com.github.WeeiaEduTeam.InfinityFinanceAPI.util;

public class Util {
    public boolean isPositive(long number) {
        return number > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    public void validateArgumentsArePositive(int... values) {
        for(int i : values) {
            if(!isPositive(i))
                throw new RuntimeException(String.format("Provided value %d is not positive!", i));
        }
    }
}
