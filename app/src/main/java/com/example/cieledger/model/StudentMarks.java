package com.example.cieledger.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

@Entity(tableName = "student_marks")
public class StudentMarks {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int classId;
    private String usn;
    private String name;

    // Theory marks
    private float internal1;   // out of 50
    private float internal2;   // out of 50
    private float la1;         // out of 10
    private float la2;         // out of 10

    // Lab marks (INTEGRATED only)
    private float observation; // out of 10
    private float record;      // out of 10
    private float labInternal; // out of 30

    // Attendance
    private float attendance;  // percentage

    // ── Constructors ─────────────────────────────────────────────
    public StudentMarks() {}

    @Ignore
    public StudentMarks(int classId) {
        this.classId = classId;
        this.usn = "";
        this.name = "";
    }

    // ── CIE Calculation ──────────────────────────────────────────

    // Best of I1/I2 reduced to 30
    public float getInternalReduced() {
        float best = Math.max(internal1, internal2);
        return (best / 50f) * 30f;
    }

    // Best of LA1/LA2 reduced to 20
    public float getLaReduced() {
        float best = Math.max(la1, la2);
        return (best / 10f) * 20f;
    }

    // Theory CIE = internal(30) + LA(20) = /50
    public float getTheoryCIE() {
        return getInternalReduced() + getLaReduced();
    }

    // Lab CIE = obs(10) + rec(10) + labInternal(30) = /50
    public float getLabCIE() {
        return observation + record + labInternal;
    }

    // Final CIE based on subject type
    public float getFinalCIE(String subjectType) {
        if ("INTEGRATED".equals(subjectType)) {
            return (getTheoryCIE() + getLabCIE()) / 2f;
        }
        return getTheoryCIE();
    }

    public boolean isAttendanceEligible() {
        return attendance >= 85f;
    }

    public boolean isCIEEligible(String subjectType) {
        return getFinalCIE(subjectType) >= 20f;
    }

    public boolean isEligible(String subjectType) {
        return isAttendanceEligible() && isCIEEligible(subjectType);
    }

    public String getEligibilityStatus(String subjectType) {
        return isEligible(subjectType) ? "ELIGIBLE" : "NOT ELIGIBLE";
    }

    // ── Getters & Setters ────────────────────────────────────────
    public int getId()                   { return id; }
    public void setId(int id)            { this.id = id; }

    public int getClassId()              { return classId; }
    public void setClassId(int v)        { this.classId = v; }

    public String getUsn()               { return usn; }
    public void setUsn(String v)         { this.usn = v; }

    public String getName()              { return name; }
    public void setName(String v)        { this.name = v; }

    public float getInternal1()          { return internal1; }
    public void setInternal1(float v)    { this.internal1 = v; }

    public float getInternal2()          { return internal2; }
    public void setInternal2(float v)    { this.internal2 = v; }

    public float getLa1()                { return la1; }
    public void setLa1(float v)          { this.la1 = v; }

    public float getLa2()                { return la2; }
    public void setLa2(float v)          { this.la2 = v; }

    public float getObservation()        { return observation; }
    public void setObservation(float v)  { this.observation = v; }

    public float getRecord()             { return record; }
    public void setRecord(float v)       { this.record = v; }

    public float getLabInternal()        { return labInternal; }
    public void setLabInternal(float v)  { this.labInternal = v; }

    public float getAttendance()         { return attendance; }
    public void setAttendance(float v)   { this.attendance = v; }
}
