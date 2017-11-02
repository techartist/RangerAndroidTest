package rangerhealth.com.rangerandroidtest;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import rangerhealth.com.rangerandroidtest.model.User;
import rangerhealth.com.rangerandroidtest.model.UserList;

/**
 * Created by kristywelsh on 11/2/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<UserHolder> {

    private UserList userList;

    public RecyclerAdapter(UserList userList) {
        this.userList = userList;
    }

    public void setUserList(UserList userList) {
        this.userList = userList;
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ranger_item_row, parent, false);
        return new UserHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        User user = userList.get(position);
        holder.bindPhoto(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}

class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.item_image)
    ImageView mItemImage;

    @BindView(R.id.item_name)
    TextView mItemName;

    public UserHolder(View v) {
        super(v);
        ButterKnife.bind(this,v);
        v.setOnClickListener(this);
    }

    public void bindPhoto(final User user) {
        mItemName.setText(user.getName());
        Picasso.with(mItemImage.getContext()).load(user.getAvatar()).into(mItemImage);

    }

    @Override
    public void onClick(View v) {
        Log.d("RecyclerView", "CLICK!");
    }
}
