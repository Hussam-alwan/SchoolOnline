package com.bootcamp.onlineschool.comparator;

import com.bootcamp.onlineschool.model.Student;
import java.util.Comparator;

public class StudentEmailDomainComparator implements Comparator<Student> {

    @Override
    public int compare(Student s1, Student s2) {
        String domain1 = getDomain(s1.getEmail());
        String domain2 = getDomain(s2.getEmail());

        int domainComparison = domain1.compareToIgnoreCase(domain2);

        if (domainComparison != 0) {
            return domainComparison;
        }
        return s1.getName().compareToIgnoreCase(s2.getName());
    }

    private String getDomain(String email) {
        if (email == null || !email.contains("@")) {
            return "";
        }
        return email.substring(email.indexOf("@") + 1);
    }
}