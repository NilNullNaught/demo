package com.heyunetwork.demo.util.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StaffConstant {
    public static List staffQueryFields = Arrays.asList("name", "sex", "department", "formal_schooling");

    public static List staffSortFields = Arrays.asList("name", "sex", "department", "formal_schooling","gmt_create","gmt_modified");

    public static List trainingRecordFields = Arrays.asList("training_date", "training_teacher", "training_content");

    public static List trainingRecordSortFields = Arrays.asList("training_date", "training_teacher", "training_content","gmt_create","gmt_modified");
}
