package com.scleroid.nemai.viewholders;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ramotion.garlandview.header.HeaderDecorator;
import com.ramotion.garlandview.header.HeaderItem;
import com.ramotion.garlandview.inner.InnerLayoutManager;
import com.ramotion.garlandview.inner.InnerRecyclerView;
import com.scleroid.nemai.AppDatabase;
import com.scleroid.nemai.R;
import com.scleroid.nemai.adapter.AddressRecyclerViewAdapter;
import com.scleroid.nemai.controller.OrderLab;
import com.scleroid.nemai.fragment.AddressFragment;
import com.scleroid.nemai.models.Address;
import com.scleroid.nemai.models.OrderedCourier;
import com.scleroid.nemai.models.Parcel;
import com.scleroid.nemai.models.PinCode;
import com.scleroid.nemai.outer.RecyclerItemClickListener;
import com.scleroid.nemai.utils.Events;
import com.scleroid.nemai.utils.GlobalBus;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

/**
 * This is The viewholder class for the RecyclerViewPager
 * which extends HeaderItem, which extends RecyclerView.ViewHolder
 * It holds Data to be displayed on the innerRecyclerView & the connections it needs to be making
 * @author Ganesh
 * @since 15-11-2017
 * @see android.support.v7.widget.RecyclerView.ViewHolder
 * @see HeaderItem
 *
 */

public class ParcelHolder extends HeaderItem {

    /**
     * TAG variable used to generate log for the logcat
     *
     * @see Log
     */
    private static final String TAG = "ParcelHolder";

    /**
     * start Ratio for the middle view,
     */
    private final static float MIDDLE_RATIO_START = 0.7f;
    /**
     * Maximum allowed ratio of the middle
     */
    private final static float MIDDLE_RATIO_MAX = 0.1f;
    /**
     * Difference between max ratio of middle & start ratio of the middle
     */
    private final static float MIDDLE_RATIO_DIFF = MIDDLE_RATIO_START - MIDDLE_RATIO_MAX;
    /**
     * start Ratio for the footer view,
     */
    private final static float FOOTER_RATIO_START = 1.1f;
    /**
     * Maximum allowed ratio of the footer
     */
    private final static float FOOTER_RATIO_MAX = 0.35f;
    /**
     * Difference between max ratio of footer & start ratio of footer
     */
    private final static float FOOTER_RATIO_DIFF = FOOTER_RATIO_START - FOOTER_RATIO_MAX;
    /**
     * start Ratio for the answer view,
     */
    private final static float ANSWER_RATIO_START = 0.75f;
    /**
     * Maximum allowed ratio of the answer View
     */
    private final static float ANSWER_RATIO_MAX = 0.35f;
    /**
     * Difference between max ratio of answer view & start ratio of answer View
     */
    private final static float ANSWER_RATIO_DIFF = ANSWER_RATIO_START - ANSWER_RATIO_MAX;
    private final List<View> mMiddleCollapsible = new ArrayList<>(2);
    private final TextView noAddressTitleTextView;
    private final TextView noAddressSubtitleTextView;
    protected List<Address> selectedAddressList = new ArrayList<>();
    GestureDetectorCompat gestureDetector;
    android.view.ActionMode actionMode;
    private View mHeader;
    private View mHeaderAlpha;
    private InnerRecyclerView innerRecyclerView;
    private TextView mHeaderCaption1;
    private TextView mHeaderCaption2;
    private TextView source;
    private TextView destination;
    private TextView cost;
    private TextView edit;
    private View mMiddle;
    private View mMiddleEdit;
    private View mFooter;
    private int m10dp;
    private int m120dp;
    private int mTitleSize1;
    private int mTitleSize2;
    private List<Address> tail;
    private OrderedCourier thatOrderedCourier;
    private View mNewAddressButton;
    private boolean mIsScrolling;
    private View mEmptyView;
    private AddressRecyclerViewAdapter adapter;
    private boolean isMultiSelect = false;
    private Parcel header;
    private OrderedCourier mOrderedCourier;

    public ParcelHolder(View itemView, RecyclerView.RecycledViewPool pool) {
        super(itemView);
        initHeader(itemView);
        initRecycerView(itemView, pool);

        //Init Empty View Message
        noAddressTitleTextView = itemView.findViewById(R.id.no_address_title);
        noAddressSubtitleTextView = itemView.findViewById(R.id.no_address_subtitle);


        // Init fonts

        //  selectedAddressList = new ArrayList<>();
        //  DataBindingUtil.bind(((FrameLayout) mHeader).getChildAt(0));
    }

    public List<Address> getSelectedAddressList() {
        return selectedAddressList;
    }

    public void setSelectedAddressList(List<Address> selectedAddressList) {
        this.selectedAddressList = selectedAddressList;
    }

    public void updateSelectedAddressList(Address selectedAddressList) {
        this.selectedAddressList.add(selectedAddressList);
    }

    public void initRecycerView(View itemView, RecyclerView.RecycledViewPool pool) {
        // Init RecyclerView
        innerRecyclerView = itemView.findViewById(R.id.recycler_view);
        innerRecyclerView.setRecycledViewPool(pool);
        innerRecyclerView.setAdapter(new AddressRecyclerViewAdapter());

        innerRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                mIsScrolling = newState != RecyclerView.SCROLL_STATE_IDLE;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                onItemScrolled(recyclerView, dx, dy);
            }
        });

        innerRecyclerView.setOnClickListener(v -> {

        });
        innerRecyclerView.addItemDecoration(new HeaderDecorator(
                itemView.getContext().getResources().getDimensionPixelSize(R.dimen.inner_item_height_decoration),
                itemView.getContext().getResources().getDimensionPixelSize(R.dimen.inner_item_offset)));
    }

    public void initHeader(View itemView) {
        // Init header
        m10dp = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.dp10);
        m120dp = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.dp120);
        mTitleSize1 = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.header_title2_text_size);
        mTitleSize2 = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.header_title2_name_text_size);

        mHeader = itemView.findViewById(R.id.header);
        mHeaderAlpha = itemView.findViewById(R.id.header_alpha);
        mNewAddressButton = itemView.findViewById(R.id.new_address_button);


        mHeaderCaption1 = itemView.findViewById(R.id.header_shipment_title_1);
        mHeaderCaption2 = itemView.findViewById(R.id.header_shipment_title_2);
        source = itemView.findViewById(R.id.tv_source);
        destination = itemView.findViewById(R.id.tv_destination);
        cost = itemView.findViewById(R.id.tv_cost);
        edit = itemView.findViewById(R.id.edit_image_button);


        mMiddle = itemView.findViewById(R.id.header_middle);
        mMiddleEdit = itemView.findViewById(R.id.header_middle_edit);
        mFooter = itemView.findViewById(R.id.header_footer);

        mNewAddressButton = itemView.findViewById(R.id.new_address_button);

        //  mMiddleCollapsible.add((View)mAvatar.getParent());
        mMiddleCollapsible.add((View) cost.getParent());
    }

    @Override
    public View getHeader() {
        return mHeader;
    }

    @Override
    public View getHeaderAlphaView() {
        return mHeaderAlpha;
    }

    @Override
    public boolean isScrolling() {
        return mIsScrolling;
    }

    @Override
    public InnerRecyclerView getViewGroup() {
        return innerRecyclerView;
    }

    public void setContent(@NonNull List<Address> innerDataList, final Parcel parcel, int position, int size, OrderedCourier orderedCourier) {
        final Context context = itemView.getContext();
        itemView.setTag(parcel);
        //TODO find position & re assign the value to it, to retain from the view changes

        header = parcel;
        tail = innerDataList;
        thatOrderedCourier = orderedCourier;
        if (thatOrderedCourier != null && thatOrderedCourier.getAddress() != null)
            selectedAddressList.add(thatOrderedCourier.getAddress());


        //  Crashlytics.getInstance().crash(); // Force a crash

        if (tail != null && !tail.isEmpty()) {
            noAddressTitleTextView.setVisibility(View.GONE);
            noAddressSubtitleTextView.setVisibility(View.GONE);
            innerRecyclerView.setVisibility(View.VISIBLE);
            setupInsideRecyclerView();
        } else {
            noAddressTitleTextView.setVisibility(View.VISIBLE);
            noAddressSubtitleTextView.setVisibility(View.VISIBLE);
            innerRecyclerView.setVisibility(View.GONE);

        }

        //gestureDetector = new GestureDetectorCompat(this.getHeader().getContext(), new RecyclerViewDemoOnGestureListener());

        final String title1 = bindNumber(position, size);

        final Spannable title2 = new SpannableString(title1 + " - Rs. " + parcel.getInvoice());
        title2.setSpan(new AbsoluteSizeSpan(mTitleSize1), 0, title1.length(), SPAN_INCLUSIVE_INCLUSIVE);
        title2.setSpan(new AbsoluteSizeSpan(mTitleSize2), title1.length(), title2.length(), SPAN_INCLUSIVE_INCLUSIVE);
        title2.setSpan(new ForegroundColorSpan(Color.argb(204, 255, 255, 255)), title1.length(), title2.length(), SPAN_INCLUSIVE_INCLUSIVE);

        mHeaderCaption1.setText(title1);
        mHeaderCaption2.setText(title2);

        source.setText(parcel.getSourcePin());//TODO COnvert Pincode to room , get source city instead of pincod,e & store selected object instead of text
        destination.setText(parcel.getDestinationPin());
        cost.setText("Rs. " + parcel.getInvoice());//TODO get delivery price, not invoice


        mNewAddressButton.setOnClickListener(v -> {


            FragmentManager fm = ((FragmentActivity) getHeader().getContext()).getFragmentManager();
            Parcel parcel1 = header;
            PinCode pincode = parcel1.getDestinationPinCode();
            DialogFragment dialog = AddressFragment.newInstance(pincode.getLocation(), pincode.getPincode(), pincode.getState());
            dialog.show(fm, "adad");

        });

    }

    private void setupInsideRecyclerView() {
        innerRecyclerView.setLayoutManager(new InnerLayoutManager());
        adapter = (AddressRecyclerViewAdapter) innerRecyclerView.getAdapter();
        adapter.addData(tail, selectedAddressList);

        innerRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getHeader().getContext(), innerRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (view.getId() == R.id.edit_image_button) {
                    Toasty.error(getHeader().getContext(), "It works ").show();
                }
                //adapter.toggleSelection(position);
                multi_select(position);

                //  Toasty.makeText(getHeader().getContext(), "Details Page", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

                //multi_select(position);

            }
        }));
    }

    /**
     * Sets the selected position in the
     *
     * @param position the current position, for which this method is called
     * @param whatToDo is the position selected or not, true if selected, otherwise false
     * @see java.util.Map which later changed to
     * @see android.util.SparseBooleanArray
     */
    private void setSelectedAddress(int position, boolean whatToDo) {
        Events.selectionMap selectionMap = new Events.selectionMap(position, whatToDo);
        GlobalBus.getBus().post(selectionMap);
    }

    /**
     * @param position
     * @deprecated Will be removed soon
     * As not functioning solution due to reusing of the viewHolder
     */
    public void multi_select(int position) {
        boolean whatToDo = false;// if false, delete  it from db, if true, add it
        try {

            if (selectedAddressList.isEmpty()) {
                Log.d(TAG, "list empty " + selectedAddressList.isEmpty());
                thatOrderedCourier = new OrderedCourier(header, tail.get(position));
                selectedAddressList.add(tail.get(position));
                whatToDo = true;


            } else {
                Log.d(TAG, "list not  empty " + selectedAddressList.isEmpty());

                if (selectedAddressList.contains(tail.get(position))) {
                    selectedAddressList.remove(tail.get(position));
                    thatOrderedCourier.setAddress(null);
                    whatToDo = false;

                } else {
                    selectedAddressList.clear();
                    thatOrderedCourier.setAddress(tail.get(position));
                    selectedAddressList.add(tail.get(position));
                    whatToDo = true;


                }


            }
            setSelectedAddress(getAdapterPosition(), whatToDo);
            if (whatToDo)
                OrderLab.addOrder(thatOrderedCourier, AppDatabase.getAppDatabase(getHeader().getContext()));
            else
                OrderLab.deleteOrderedCourier(thatOrderedCourier, AppDatabase.getAppDatabase(getHeader().getContext()));

        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "Array Out of Bound " + e);
        }
        refreshAdapter(position);

    }

    public void refreshAdapter(int position) {
        adapter.updateSelectedData(position, selectedAddressList);
    }

    public String bindNumber(int position, int size) {
        return String.format("Shipment %d of %d", position + 1, size);
    }

    void clearContent() {
        // Glide.clear(mAvatar);
        ((AddressRecyclerViewAdapter) innerRecyclerView.getAdapter()).clearData();
    }

    private float computeRatio(RecyclerView recyclerView) {
        final View child0 = recyclerView.getChildAt(0);
        final int pos = recyclerView.getChildAdapterPosition(child0);
        if (pos != 0) {
            return 0;
        }

        final int height = child0.getHeight();
        final float y = Math.max(0, child0.getY());
        return y / height;
    }

    private void onItemScrolled(RecyclerView recyclerView, int dx, int dy) {
        final float ratio = computeRatio(recyclerView);

        final float footerRatio = Math.max(0, Math.min(FOOTER_RATIO_START, ratio) - FOOTER_RATIO_DIFF) / FOOTER_RATIO_MAX;
        //final float avatarRatio = Math.max(0, Math.min(AVATAR_RATIO_START, ratio) - AVATAR_RATIO_DIFF) / AVATAR_RATIO_MAX;
        final float answerRatio = Math.max(0, Math.min(ANSWER_RATIO_START, ratio) - ANSWER_RATIO_DIFF) / ANSWER_RATIO_MAX;
        final float middleRatio = Math.max(0, Math.min(MIDDLE_RATIO_START, ratio) - MIDDLE_RATIO_DIFF) / MIDDLE_RATIO_MAX;

        ViewCompat.setPivotY(mFooter, 0);
        ViewCompat.setScaleY(mFooter, footerRatio);
        ViewCompat.setAlpha(mFooter, footerRatio);

        ViewCompat.setPivotY(mMiddleEdit, mMiddleEdit.getHeight());
        ViewCompat.setScaleY(mMiddleEdit, 1f - answerRatio);
        ViewCompat.setAlpha(mMiddleEdit, 0.5f - answerRatio);

        ViewCompat.setAlpha(mHeaderCaption1, answerRatio);
        ViewCompat.setAlpha(mHeaderCaption2, 1f - answerRatio);

        final View mc2 = mMiddleCollapsible.get(0);
        ViewCompat.setPivotX(mc2, 0);
        ViewCompat.setPivotY(mc2, mc2.getHeight() / 2);

        for (final View view : mMiddleCollapsible) {
            ViewCompat.setScaleX(view, middleRatio);
            ViewCompat.setScaleY(view, middleRatio);
            ViewCompat.setAlpha(view, middleRatio);
        }
    }

    public void setContent(Parcel parcel, int position, int size, OrderedCourier orderedCourier) {
        setContent(new ArrayList<>(), parcel, position, size, orderedCourier);
    }



}