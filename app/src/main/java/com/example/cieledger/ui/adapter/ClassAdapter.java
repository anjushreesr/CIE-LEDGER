package com.example.cieledger.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cieledger.databinding.ItemClassCardBinding;
import com.example.cieledger.model.ClassEntity;

import java.util.ArrayList;
import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    public interface ClassClickListener {
        void onClassClick(ClassEntity c);
        void onEditClick(ClassEntity c);
        void onDeleteClick(ClassEntity c);
    }

    private List<ClassEntity> classes = new ArrayList<>();
    private final ClassClickListener listener;

    public ClassAdapter(ClassClickListener listener) {
        this.listener = listener;
    }

    // CRITICAL: use new list + notifyDataSetChanged to ensure UI updates
    public void setClasses(List<ClassEntity> newList) {
        this.classes = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemClassCardBinding b = ItemClassCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ClassViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        holder.bind(classes.get(position));
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    class ClassViewHolder extends RecyclerView.ViewHolder {
        private final ItemClassCardBinding b;

        ClassViewHolder(ItemClassCardBinding b) {
            super(b.getRoot());
            this.b = b;
        }

        void bind(ClassEntity c) {
            b.tvCourseName.setText(c.getCourseName());
            b.tvSemSection.setText("Sem " + c.getSemester() + "  ·  Sec " + c.getSection());
            b.tvStudentCount.setText(c.getNumStudents() + " students");
            b.tvType.setText(c.getSubjectType());
            b.tvInitial.setText(String.valueOf(c.getCourseName().charAt(0)).toUpperCase());

            b.getRoot().setOnClickListener(v -> listener.onClassClick(c));
            b.btnEdit.setOnClickListener(v -> listener.onEditClick(c));
            b.btnDelete.setOnClickListener(v -> listener.onDeleteClick(c));

            // Animate entry
            b.getRoot().setAlpha(0f);
            b.getRoot().setTranslationY(30f);
            b.getRoot().animate()
                    .alpha(1f).translationY(0f)
                    .setDuration(300)
                    .setStartDelay(getAdapterPosition() * 50L)
                    .start();
        }
    }
}
