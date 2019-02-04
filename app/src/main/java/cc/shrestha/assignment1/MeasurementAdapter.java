package cc.shrestha.assignment1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;

public class MeasurementAdapter extends RecyclerView.Adapter<MeasurementAdapter.MeasurementViewHolder>{
    private Context mContext;
    private ArrayList<MeasurementItem> mMeasurementList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){mListener = listener;}

    public MeasurementAdapter(Context context, ArrayList<MeasurementItem> measurementList){
        mContext = context;
        mMeasurementList = measurementList;
    }

    @Override
    public MeasurementViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.measurement_item,viewGroup,false);
        return new MeasurementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MeasurementViewHolder holder, int position){
        MeasurementItem currentItem = mMeasurementList.get(position);

        holder.mTextViewDate.setText(currentItem.getDate());
        holder.mTextViewTime.setText(currentItem.getTime());
        holder.mTextViewSystolic.setText(""+currentItem.getSystolicPressure());
        holder.mTextViewDiastolic.setText(""+currentItem.getDiastolicPressure());
        holder.mTextViewHeart.setText(""+currentItem.getHeartRate());
        holder.mTextViewComment.setText(currentItem.getComment());
    }

    @Override
    public int getItemCount(){return mMeasurementList.size();}

    public class MeasurementViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextViewDate, mTextViewTime, mTextViewSystolic, mTextViewDiastolic, mTextViewHeart, mTextViewComment;
        public MeasurementViewHolder(View itemView){
            super(itemView);
            mTextViewDate = itemView.findViewById(R.id.textViewDate);
            mTextViewTime = itemView.findViewById(R.id.textViewTime);
            mTextViewSystolic = itemView.findViewById(R.id.textViewSystolic);
            mTextViewDiastolic = itemView.findViewById(R.id.textViewDiastolic);
            mTextViewHeart = itemView.findViewById(R.id.textViewHeart);
            mTextViewComment = itemView.findViewById(R.id.textViewComment);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }

    }
}
