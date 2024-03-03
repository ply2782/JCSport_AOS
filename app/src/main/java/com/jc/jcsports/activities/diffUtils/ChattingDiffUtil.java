package com.jc.jcsports.activities.diffUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.jc.jcsports.model.ChatModel;
import com.jc.jcsports.model.chat.ChattingModel;

import java.util.List;

public class ChattingDiffUtil extends DiffUtil.Callback {
    private List<ChattingModel> oldItems;
    private List<ChattingModel> newItems;

    public ChattingDiffUtil(List<ChattingModel> oldItems, List<ChattingModel> newItems) {
        this.oldItems = oldItems;
        this.newItems = newItems;
    }

    @Override
    public int getOldListSize() {
        return oldItems.size();
    }

    @Override
    public int getNewListSize() {
        return newItems.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItems.get(oldItemPosition).equals(newItems.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItems.get(oldItemPosition).equals(newItems.get(newItemPosition));
    }
}

