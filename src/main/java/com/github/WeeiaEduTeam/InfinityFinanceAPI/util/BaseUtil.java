package com.github.WeeiaEduTeam.InfinityFinanceAPI.util;

public class BaseUtil {
    protected boolean isPositive(long number) {
        return number > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    protected void validateArgumentsArePositive(int... values) {
        for(int i : values) {
            if(!isPositive(i))
                throw new RuntimeException(String.format("Provided value %d is not positive!", i));
        }
    }
}
