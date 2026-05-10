package com.example.cieledger.ui.adapter;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cieledger.databinding.ItemMarksRowBinding;
import com.example.cieledger.model.StudentMarks;

import java.util.ArrayList;
import java.util.List;

public class MarksAdapter extends RecyclerView.Adapter<MarksAdapter.MarksViewHolder> {

    // ✅ FIX: moved interfaces here (outside ViewHolder)
    public interface StringConsumer { void accept(String val); }
    public interface FloatConsumer  { void accept(float val); }

    private final List<StudentMarks> items = new ArrayList<>();
    private final String subjectType;

    public MarksAdapter(String subjectType) {
        this.subjectType = subjectType;
    }

    public void setStudents(List<StudentMarks> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }

    public List<StudentMarks> getCurrentList() {
        return new ArrayList<>(items);
    }

    @NonNull
    @Override
    public MarksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMarksRowBinding b = ItemMarksRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MarksViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull MarksViewHolder holder, int position) {
        holder.bind(items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // ── ViewHolder ───────────────────────────────────────────────
    class MarksViewHolder extends RecyclerView.ViewHolder {
        private final ItemMarksRowBinding b;
        private final List<TextWatcher> watchers = new ArrayList<>();

        MarksViewHolder(ItemMarksRowBinding b) {
            super(b.getRoot());
            this.b = b;
        }

        void bind(StudentMarks s, int position) {
            removeAllWatchers();

            b.tvSlNo.setText(String.valueOf(position + 1));

            b.etLab.setVisibility("INTEGRATED".equals(subjectType) ? View.VISIBLE : View.GONE);

            setTextSilent(b.etUsn,        s.getUsn());
            setTextSilent(b.etName,       s.getName());
            setTextSilent(b.etInternal1,  floatToStr(s.getInternal1()));
            setTextSilent(b.etInternal2,  floatToStr(s.getInternal2()));
            setTextSilent(b.etLa1,        floatToStr(s.getLa1()));
            setTextSilent(b.etLa2,        floatToStr(s.getLa2()));
            setTextSilent(b.etAttendance, floatToStr(s.getAttendance()));

            if ("INTEGRATED".equals(subjectType)) {
                setTextSilent(b.etLab, floatToStr(s.getLabInternal()));
            }

            refreshCIE(s);

            attachStringWatcher(b.etUsn, val -> {
                int idx = getAdapterPosition();
                if (idx >= 0 && idx < items.size()) items.get(idx).setUsn(val);
            });

            attachStringWatcher(b.etName, val -> {
                int idx = getAdapterPosition();
                if (idx >= 0 && idx < items.size()) items.get(idx).setName(val);
            });

            attachFloatWatcher(b.etInternal1, 50f, val -> {
                int idx = getAdapterPosition();
                if (idx >= 0 && idx < items.size()) {
                    items.get(idx).setInternal1(val);
                    refreshCIE(items.get(idx));
                }
            });

            attachFloatWatcher(b.etInternal2, 50f, val -> {
                int idx = getAdapterPosition();
                if (idx >= 0 && idx < items.size()) {
                    items.get(idx).setInternal2(val);
                    refreshCIE(items.get(idx));
                }
            });

            attachFloatWatcher(b.etLa1, 10f, val -> {
                int idx = getAdapterPosition();
                if (idx >= 0 && idx < items.size()) {
                    items.get(idx).setLa1(val);
                    refreshCIE(items.get(idx));
                }
            });

            attachFloatWatcher(b.etLa2, 10f, val -> {
                int idx = getAdapterPosition();
                if (idx >= 0 && idx < items.size()) {
                    items.get(idx).setLa2(val);
                    refreshCIE(items.get(idx));
                }
            });

            attachFloatWatcher(b.etAttendance, 100f, val -> {
                int idx = getAdapterPosition();
                if (idx >= 0 && idx < items.size()) {
                    items.get(idx).setAttendance(val);
                    refreshCIE(items.get(idx));
                }
            });

            if ("INTEGRATED".equals(subjectType)) {
                attachFloatWatcher(b.etLab, 30f, val -> {
                    int idx = getAdapterPosition();
                    if (idx >= 0 && idx < items.size()) {
                        items.get(idx).setLabInternal(val);
                        refreshCIE(items.get(idx));
                    }
                });
            }
        }

        private void refreshCIE(StudentMarks s) {
            float cie = s.getFinalCIE(subjectType);
            b.tvCIE.setText(String.format("%.1f", cie));

            boolean eligible = s.isEligible(subjectType);
            if (eligible) {
                b.tvStatus.setText("✓ ELIGIBLE");
                b.tvStatus.setTextColor(Color.parseColor("#10B981"));
                b.tvCIE.setTextColor(Color.parseColor("#10B981"));
            } else {
                b.tvStatus.setText("✗ NOT ELG");
                b.tvStatus.setTextColor(Color.parseColor("#F43F5E"));
                b.tvCIE.setTextColor(Color.parseColor("#F43F5E"));
            }
        }

        private void attachStringWatcher(EditText et, StringConsumer consumer) {
            TextWatcher w = new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
                public void onTextChanged(CharSequence s, int st, int b, int c) {}
                public void afterTextChanged(Editable s) {
                    consumer.accept(s.toString());
                }
            };
            et.addTextChangedListener(w);
            watchers.add(w);
        }

        private void attachFloatWatcher(EditText et, float max, FloatConsumer consumer) {
            TextWatcher w = new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
                public void onTextChanged(CharSequence s, int st, int b2, int c) {}
                public void afterTextChanged(Editable s) {
                    String str = s.toString();
                    float val = 0f;
                    try { val = Float.parseFloat(str); } catch (Exception ignored) {}

                    val = Math.min(val, max);

                    if (!str.isEmpty() && Float.parseFloat(str) > max) {
                        et.setTextColor(Color.parseColor("#F43F5E"));
                    } else {
                        et.setTextColor(Color.WHITE);
                    }

                    consumer.accept(val);
                }
            };
            et.addTextChangedListener(w);
            watchers.add(w);
        }

        private void removeAllWatchers() {}

        private void setTextSilent(EditText et, String text) {
            if (text == null) text = "";
            if (!et.getText().toString().equals(text)) {
                et.setText(text);
            }
        }

        private String floatToStr(float f) {
            if (f == 0f) return "";
            if (f == (long) f) return String.valueOf((long) f);
            return String.valueOf(f);
        }
    }
}