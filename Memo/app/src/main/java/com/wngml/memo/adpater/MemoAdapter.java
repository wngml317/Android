package com.wngml.memo.adpater;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.wngml.memo.R;
import com.wngml.memo.UpdateActivity;
import com.wngml.memo.data.DatabaseHandler;
import com.wngml.memo.model.Memo;

import java.util.List;

// 1. RecyclerView.Adapter 를 상속받는다.

// 5. 우리가 만든 ViewHolder의 타입으로 RecyclerView.Adapter의 타입을 설정해준다.
public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {

    // 4. 어댑터 클래스의 멤버변수와 생성자를 만들어 준다.
    Context context;
    List<Memo> memoList;

    int deleteIndex;

    public MemoAdapter(Context context, List<Memo> memoList) {
        this.context = context;
        this.memoList = memoList;
    }

    // 6. 함수 구현
    @NonNull
    @Override
    public MemoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_row, parent, false);
        return new MemoAdapter.ViewHolder(view);
    }

    // 메모리에 있는 데이터(리스트)를 화면에 표시하는 함수
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Memo memo = memoList.get(position);

        holder.txtTitle.setText(memo.title);
        holder.txtContent.setText(memo.content);

    }

    @Override
    public int getItemCount() {
        return memoList.size();
    }

    // 2. row.xml 화면에 있는 뷰를 연결시키는 클래스
    // 화면과 연결할 자바 변수를 만든다.
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtContent;
        ImageView imgDelete;
        CardView cardView;

        // 3. 생성자 안에 연결시키는 코드 작성
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtContent = itemView.findViewById(R.id.txtContent);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            cardView = itemView.findViewById(R.id.cardView);

            // cardView 를 눌렀을 때 수정 페이지로 이동
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 1. 유저가 몇번째 행을 클릭했는지 인덱스로 알려준다.
                    int index = getAdapterPosition();

                    Log.i("MyMemo", "index : " + index);

                    // 2. 이 인덱스에 저장되어 있는 데이터를 가져온다.
                    Memo memo = memoList.get(index);

                    // 3. 아이디, 제목, 내용을 수정하는 화면으로 데이터를 넘겨준다.
                    Intent intent = new Intent(context, UpdateActivity.class);

                    // 객체를 putExtra 로 넘겨줄때는
                    // 해당 클래스에 implements Serializable 해줘야한다.
                    intent.putExtra("memo", memo);

                    context.startActivity(intent);
                }
            });

            // 엑스 이미지를 눌렀을 때 해당 메모를 삭제
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    deleteIndex = getAdapterPosition();

                    AlertDialog.Builder alert = new AlertDialog.Builder(context);

                    alert.setTitle("메모 삭제");
                    alert.setMessage("정말 삭제하시겠습니까?");

                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            DatabaseHandler db = new DatabaseHandler(context);

                            Memo memo = memoList.get(deleteIndex);

                            db.deleteMemo(memo.id);

                            db.close();

                            memoList.remove(deleteIndex);

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
