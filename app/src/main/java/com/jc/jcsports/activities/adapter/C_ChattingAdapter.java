package com.jc.jcsports.activities.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jc.jcsports.R;
import com.jc.jcsports.activities.diffUtils.ChattingDiffUtil;
import com.jc.jcsports.bundle.SignUpBundle;
import com.jc.jcsports.databinding.ItemCEnterBinding;
import com.jc.jcsports.databinding.ItemCExitBinding;
import com.jc.jcsports.databinding.ItemCMeBinding;
import com.jc.jcsports.databinding.ItemCYouBinding;
import com.jc.jcsports.databinding.ItemEmptyBinding;
import com.jc.jcsports.model.LoginModel;
import com.jc.jcsports.model.chat.ChattingModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class C_ChattingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int myUid = 0;
    private List<ChattingModel> c_Model = new ArrayList<>();

    public void setC_New(List<ChattingModel> newC_Model) {
//        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ChattingDiffUtil(c_Model, newC_Model));
        c_Model.clear();
        c_Model.addAll(newC_Model);
//        diffResult.dispatchUpdatesTo(this);
        notifyDataSetChanged();
    }

    public C_ChattingAdapter(Context context) {
        this.context = context;
        LoginModel loginModel = (LoginModel) SignUpBundle.getBundle().getSerializable("loginModel");
        myUid = loginModel.s_Number;

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return c_Model.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChattingModel.MessageType messageType = c_Model.get(position).c_MessageType;
        int c_S_Number = c_Model.get(position).s_Number;
        if (messageType == ChattingModel.MessageType.MESSAGE) {
            if (c_S_Number == myUid) {
                return R.layout.item_c_me;
            } else {
                return R.layout.item_c_you;
            }
        } else if (messageType == ChattingModel.MessageType.ENTER) {
            return R.layout.item_c_enter;
        } else if (messageType == ChattingModel.MessageType.EXIT) {
            return R.layout.item_c_exit;
        } else {
            return R.layout.item_empty;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        switch (viewType) {
            case R.layout.item_c_me:
                ItemCMeBinding itemMeBinding = ItemCMeBinding.inflate(inflater, viewGroup, false);
                return new ItemCMeViewHolder(itemMeBinding);

            case R.layout.item_c_you:
                ItemCYouBinding itemYouBinding = ItemCYouBinding.inflate(inflater, viewGroup, false);
                return new ItemCYouViewHolder(itemYouBinding);

            case R.layout.item_c_enter:
                ItemCEnterBinding itemCEnterBinding = ItemCEnterBinding.inflate(inflater, viewGroup, false);
                return new ItemCEnterViewHolder(itemCEnterBinding);

            case R.layout.item_c_exit:
                ItemCExitBinding itemCExitBinding = ItemCExitBinding.inflate(inflater, viewGroup, false);
                return new ItemCExitViewHolder(itemCExitBinding);

            case R.layout.item_empty:
                ItemEmptyBinding itemEmptyBinding = ItemEmptyBinding.inflate(inflater, viewGroup, false);
                return new ItemEmptyViewHolder(itemEmptyBinding);


        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ChattingModel chattingModel = c_Model.get(i);
        if (viewHolder instanceof ItemCMeViewHolder) {
            ((ItemCMeViewHolder) viewHolder).onBind(chattingModel, c_Model, i);
        } else if (viewHolder instanceof ItemCYouViewHolder) {
            ((ItemCYouViewHolder) viewHolder).onBind(chattingModel, c_Model, i);
        } else if (viewHolder instanceof ItemCEnterViewHolder) {
            ((ItemCEnterViewHolder) viewHolder).onBind(chattingModel);
        } else if (viewHolder instanceof ItemCExitViewHolder) {
            ((ItemCExitViewHolder) viewHolder).onBind(chattingModel);
        } else if (viewHolder instanceof ItemEmptyViewHolder) {
            ((ItemEmptyViewHolder) viewHolder).onBind(chattingModel);
        }
    }

    public class ItemCMeViewHolder extends RecyclerView.ViewHolder {

        private ItemCMeBinding itemCMeBinding;

        public ItemCMeViewHolder(@NonNull ItemCMeBinding itemCMeBinding) {
            super(itemCMeBinding.getRoot());
            this.itemCMeBinding = itemCMeBinding;
        }


        public void onBind(ChattingModel chattingModel, List<ChattingModel> c_Model, int i) {
            int timeFormatSubstring = "0000-00-00 00:00".length();
            int officialTimeFormat = "0000-00-00".length();
            String c_OfficialTime = chattingModel.c_MessageTime.substring(0, officialTimeFormat);

            String c_Time = chattingModel.c_MessageTime.substring(0, timeFormatSubstring);
            ChattingModel.MessageType cM_Type = chattingModel.c_MessageType;
            int curS_Number = chattingModel.s_Number;

            if (0 < i && i < (c_Model.size() - 1)) {
                int a_Index = (i - 1);
                String a_Time = c_Model.get(a_Index).c_MessageTime.substring(0, timeFormatSubstring);
                int aS_Number = c_Model.get(a_Index).s_Number;
                ChattingModel.MessageType aM_Type = c_Model.get(a_Index).c_MessageType;

                int b_Index = (i + 1);
                String b_Time = c_Model.get(b_Index).c_MessageTime.substring(0, timeFormatSubstring);
                int bS_Number = c_Model.get(b_Index).s_Number;
                ChattingModel.MessageType bM_Type = c_Model.get(b_Index).c_MessageType;

                String b_OfficialTime = c_Model.get(b_Index).c_MessageTime.substring(0, officialTimeFormat);;

                if(c_OfficialTime.equals(b_OfficialTime)){
                    itemCMeBinding.dateV.setVisibility(View.GONE);
                }else{
                    itemCMeBinding.dateV.setVisibility(View.VISIBLE);
                }

                if (c_Time.equals(b_Time)) {
                    /**1*/
                    if (cM_Type.equals(bM_Type)) {
                        /**2*/
                        if (curS_Number == bS_Number) {
                            /**3*/
                            if (c_Time.equals(a_Time)) {
                                /**4*/
                                if (cM_Type.equals(aM_Type)) {
                                    if (curS_Number == aS_Number) {
                                        itemCMeBinding.tV.setVisibility(View.INVISIBLE);
                                    } else {
                                        itemCMeBinding.tV.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    itemCMeBinding.tV.setVisibility(View.VISIBLE);
                                }
                            } else {
                                /**5*/
                                itemCMeBinding.tV.setVisibility(View.VISIBLE);
                            }

                        } else {
                            /**6*/
                            if (curS_Number == aS_Number) {
                                itemCMeBinding.tV.setVisibility(View.INVISIBLE);
                            } else {
                                itemCMeBinding.tV.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        /**7*/
                        if (curS_Number == aS_Number) {
                            itemCMeBinding.tV.setVisibility(View.INVISIBLE);
                        } else {
                            itemCMeBinding.tV.setVisibility(View.VISIBLE);
                        }

                    }

                } else {


                    if (c_Time.equals(a_Time)) {
                        /**8*/
                        if (cM_Type.equals(aM_Type)) {
                            /**9*/
                            if (curS_Number == aS_Number) {
                                itemCMeBinding.tV.setVisibility(View.INVISIBLE);
                            } else {
                                itemCMeBinding.tV.setVisibility(View.VISIBLE);
                            }
                        } else {
                            /**10*/
                            itemCMeBinding.tV.setVisibility(View.VISIBLE);
                        }
                    } else {
                        /**11*/
                        itemCMeBinding.tV.setVisibility(View.VISIBLE);
                    }
                }
            } else if (i == 0) {
                /**12*/
                itemCMeBinding.tV.setVisibility(View.VISIBLE);
            }


            itemCMeBinding.setCModel(chattingModel);
            itemCMeBinding.executePendingBindings();
        }


    }

    public class ItemCYouViewHolder extends RecyclerView.ViewHolder {

        private ItemCYouBinding itemCYouBinding;
        private SimpleDateFormat s_Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        public ItemCYouViewHolder(@NonNull ItemCYouBinding itemCYouBinding) {
            super(itemCYouBinding.getRoot());
            this.itemCYouBinding = itemCYouBinding;
        }

        public void onBind(ChattingModel chattingModel, List<ChattingModel> c_Model, int i) {


            int timeFormatSubstring = "0000-00-00 00:00".length();
            int officialTimeFormat = "0000-00-00".length();
            String c_OfficialTime = chattingModel.c_MessageTime.substring(0, officialTimeFormat);

            String c_Time = chattingModel.c_MessageTime.substring(0, timeFormatSubstring);
            ChattingModel.MessageType cM_Type = chattingModel.c_MessageType;
            int curS_Number = chattingModel.s_Number;


            if (0 < i && i < (c_Model.size() - 1)) {
                int a_Index = (i - 1);
                String a_Time = c_Model.get(a_Index).c_MessageTime.substring(0, timeFormatSubstring);
                int aS_Number = c_Model.get(a_Index).s_Number;
                ChattingModel.MessageType aM_Type = c_Model.get(a_Index).c_MessageType;

                int b_Index = (i + 1);
                String b_Time = c_Model.get(b_Index).c_MessageTime.substring(0, timeFormatSubstring);
                int bS_Number = c_Model.get(b_Index).s_Number;
                ChattingModel.MessageType bM_Type = c_Model.get(b_Index).c_MessageType;


                String b_OfficialTime = c_Model.get(b_Index).c_MessageTime.substring(0, officialTimeFormat);;

                if(c_OfficialTime.equals(b_OfficialTime)){
                    itemCYouBinding.dateV.setVisibility(View.GONE);
                }else{
                    itemCYouBinding.dateV.setVisibility(View.VISIBLE);
                }


                if (c_Time.equals(b_Time)) {
                    /**1*/
                    if (cM_Type.equals(bM_Type)) {
                        /**2*/
                        if (curS_Number == bS_Number) {
                            /**3*/
                            itemCYouBinding.sPB.setVisibility(View.INVISIBLE);
                            itemCYouBinding.nV.setVisibility(View.GONE);
                            if (c_Time.equals(a_Time)) {
                                /**4*/
                                if (cM_Type.equals(aM_Type)) {
                                    if (curS_Number == aS_Number) {
                                        itemCYouBinding.tV.setVisibility(View.INVISIBLE);
                                    } else {
                                        itemCYouBinding.tV.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    itemCYouBinding.tV.setVisibility(View.VISIBLE);
                                }


                            } else {
                                /**5*/
                                itemCYouBinding.tV.setVisibility(View.VISIBLE);
                            }

                        } else {
                            itemCYouBinding.sPB.setVisibility(View.VISIBLE);
                            itemCYouBinding.nV.setVisibility(View.VISIBLE);
                            /**6*/
                            if (curS_Number == aS_Number) {
                                itemCYouBinding.tV.setVisibility(View.INVISIBLE);
                            } else {
                                itemCYouBinding.tV.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        itemCYouBinding.sPB.setVisibility(View.VISIBLE);
                        itemCYouBinding.nV.setVisibility(View.VISIBLE);
                        /**7*/
                        if (curS_Number == aS_Number) {
                            itemCYouBinding.tV.setVisibility(View.INVISIBLE);

                        } else {
                            itemCYouBinding.tV.setVisibility(View.VISIBLE);
                        }

                    }

                } else {


                    if (c_Time.equals(a_Time)) {
                        /**8*/
                        if (cM_Type.equals(aM_Type)) {
                            /**9*/
                            if (curS_Number == aS_Number) {
                                itemCYouBinding.sPB.setVisibility(View.VISIBLE);
                                itemCYouBinding.nV.setVisibility(View.VISIBLE);
                                itemCYouBinding.tV.setVisibility(View.INVISIBLE);
                            } else {
                                itemCYouBinding.tV.setVisibility(View.VISIBLE);
                            }
                        } else {
                            /**10*/
                            itemCYouBinding.tV.setVisibility(View.VISIBLE);
                        }
                    } else {
                        /**11*/
                        itemCYouBinding.tV.setVisibility(View.VISIBLE);
                    }
                }
            } else if (i == 0) {
                /**12*/
                itemCYouBinding.sPB.setVisibility(View.VISIBLE);
                itemCYouBinding.nV.setVisibility(View.VISIBLE);
                itemCYouBinding.tV.setVisibility(View.VISIBLE);
            }

            itemCYouBinding.setCModel(chattingModel);
            itemCYouBinding.executePendingBindings();
        }
    }

    public class ItemCEnterViewHolder extends RecyclerView.ViewHolder {

        private ItemCEnterBinding itemCEnterBinding;

        public ItemCEnterViewHolder(@NonNull ItemCEnterBinding itemCEnterBinding) {
            super(itemCEnterBinding.getRoot());
            this.itemCEnterBinding = itemCEnterBinding;
        }

        public void onBind(ChattingModel chattingModel) {
            itemCEnterBinding.setCModel(chattingModel);
            itemCEnterBinding.executePendingBindings();
        }
    }


    public class ItemCExitViewHolder extends RecyclerView.ViewHolder {

        private ItemCExitBinding itemCExitBinding;

        public ItemCExitViewHolder(@NonNull ItemCExitBinding itemCExitBinding) {
            super(itemCExitBinding.getRoot());
            this.itemCExitBinding = itemCExitBinding;
        }

        public void onBind(ChattingModel chattingModel) {
            itemCExitBinding.setCModel(chattingModel);
            itemCExitBinding.executePendingBindings();
        }
    }

    public class ItemEmptyViewHolder extends RecyclerView.ViewHolder {
        private ItemEmptyBinding itemEmptyBinding;

        public ItemEmptyViewHolder(@NonNull ItemEmptyBinding itemEmptyBinding) {
            super(itemEmptyBinding.getRoot());
            this.itemEmptyBinding = itemEmptyBinding;
        }

        public void onBind(ChattingModel chattingModel) {
            itemEmptyBinding.executePendingBindings();
        }
    }
}