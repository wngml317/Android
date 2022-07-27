package com.wngml.employeeapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.wngml.employeeapp.EditActivity;
import com.wngml.employeeapp.R;
import com.wngml.employeeapp.model.Employee;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {

    Context context;
    List<Employee> employeeList;

    public EmployeeAdapter(Context context, List<Employee> employeeList) {
        this.context = context;
        this.employeeList = employeeList;
    }

    @NonNull
    @Override
    public EmployeeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_row, parent, false);
        return new EmployeeAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Employee employee = employeeList.get(position);

        holder.txtName.setText(employee.name);
        holder.txtAge.setText("나이 : " + employee.age + "세");
        holder.txtSalary.setText("연봉 : $" + employee.salary);
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtAge;
        TextView txtSalary;
        CardView cardView;
        ImageView imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtAge = itemView.findViewById(R.id.txtAge);
            txtSalary = itemView.findViewById(R.id.txtSalary);
            cardView = itemView.findViewById(R.id.cardView);
            imgDelete = itemView.findViewById(R.id.imgDelete);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, EditActivity.class);

                    int index = getAdapterPosition();

                    intent.putExtra("index", index);

                    context.startActivity(intent);

                    /*
                    Employee employee = employeeList.get(index);

                    intent.putExtra("employee", employee);
                    intent.putExtra("index", index);

                    // context.startActivity(intent);
                    ((MainActivity)context).activityResultLauncher.launch(intent);
                    */
                }
            });

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(context);

                    alert.setTitle(R.string.alert_adapter_title);
                    alert.setMessage(R.string.alert_adapter_message);

                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            int index = getAdapterPosition();

                            employeeList.remove(index);

                            notifyDataSetChanged();

                        }
                    });

                    alert.setNegativeButton("No", null);

                    alert.show();
                }
            });
        }
    }
}
