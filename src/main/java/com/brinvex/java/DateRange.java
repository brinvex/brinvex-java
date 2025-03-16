package com.brinvex.java;

import java.time.LocalDate;

public sealed interface DateRange {

    LocalDate startIncl();

    LocalDate startExcl();

    LocalDate endIncl();

    LocalDate endExcl();

    default InclDateRange toIncl() {
        return this instanceof InclDateRange ? (InclDateRange) this : new InclDateRange(startIncl(), endIncl());
    }
    default StartExclDateRange toStartExcl() {
        return this instanceof StartExclDateRange ? (StartExclDateRange) this : new StartExclDateRange(startExcl(), endIncl());
    }
    default EndExclDateRange toEndExcl() {
        return this instanceof EndExclDateRange ? (EndExclDateRange) this : new EndExclDateRange(startIncl(), endExcl());
    }
    default ExclDateRange toExcl() {
        return this instanceof ExclDateRange ? (ExclDateRange) this : new ExclDateRange(startExcl(), endExcl());
    }

    record InclDateRange(@Override LocalDate startIncl, @Override LocalDate endIncl) implements DateRange {
        @Override
        public LocalDate startExcl() {
            return startIncl == null ? null : startIncl.minusDays(1);
        }

        @Override
        public LocalDate endExcl() {
            return endIncl == null ? null : endIncl.plusDays(1);
        }
    }

    record StartExclDateRange(@Override LocalDate startExcl, @Override LocalDate endIncl) implements DateRange {
        @Override
        public LocalDate startIncl() {
            return startExcl == null ? null : startExcl.plusDays(1);
        }

        @Override
        public LocalDate endExcl() {
            return endIncl == null ? null : endIncl.plusDays(1);
        }
    }

    record EndExclDateRange(@Override LocalDate startIncl, @Override LocalDate endExcl) implements DateRange {
        @Override
        public LocalDate startExcl() {
            return startIncl == null ? null : startIncl.minusDays(1);
        }

        @Override
        public LocalDate endIncl() {
            return endExcl == null ? null : endExcl.minusDays(1);
        }
    }

    record ExclDateRange(@Override LocalDate startExcl, @Override LocalDate endExcl) implements DateRange {
        @Override
        public LocalDate startIncl() {
            return startExcl == null ? null : startExcl.plusDays(1);
        }

        @Override
        public LocalDate endIncl() {
            return endExcl == null ? null : endExcl.minusDays(1);
        }
    }
}