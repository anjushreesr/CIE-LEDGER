package com.example.cieledger.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "classes")
public class ClassEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String courseName;
    private String semester;
    private String section;
    private String subjectType;   // "THEORY" or "INTEGRATED"
    private int numStudents;
    private String teacherId;     // Firebase UID
    private long createdAt;

    // ── Constructor ──────────────────────────────────────────────
    public ClassEntity(String courseName, String semester, String section,
                       String subjectType, int numStudents, String teacherId) {
        this.courseName  = courseName;
        this.semester    = semester;
        this.section     = section;
        this.subjectType = subjectType;
        this.numStudents = numStudents;
        this.teacherId   = teacherId;
        this.createdAt   = System.currentTimeMillis();
    }

    // ── Getters & Setters ────────────────────────────────────────
    public int getId()                     { return id; }
    public void setId(int id)              { this.id = id; }

    public String getCourseName()          { return courseName; }
    public void setCourseName(String v)    { this.courseName = v; }

    public String getSemester()            { return semester; }
    public void setSemester(String v)      { this.semester = v; }

    public String getSection()             { return section; }
    public void setSection(String v)       { this.section = v; }

    public String getSubjectType()         { return subjectType; }
    public void setSubjectType(String v)   { this.subjectType = v; }

    public int getNumStudents()            { return numStudents; }
    public void setNumStudents(int v)      { this.numStudents = v; }

    public String getTeacherId()           { return teacherId; }
    public void setTeacherId(String v)     { this.teacherId = v; }

    public long getCreatedAt()             { return createdAt; }
    public void setCreatedAt(long v)       { this.createdAt = v; }
}