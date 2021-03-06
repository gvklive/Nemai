package com.scleroid.nemai.data.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.scleroid.nemai.data.AppDatabase;
import com.scleroid.nemai.data.models.Parcel;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


public class ParcelLab {
    private static final String TAG = ParcelLab.class.getName();
    public static List<Parcel> parcels;
    private static ParcelLab parcelLab;
    private Parcel parcel;
    private Context context;

    public ParcelLab(Context context) {
        this.context = context.getApplicationContext();


    }

    public static Single<Parcel> newParcel(final Context context) {

        return addParcel(new Parcel(), AppDatabase.getAppDatabase(context));

    }


   /* private static List<Parcel> getAllParcels(final AppDatabase db) {
        List<Parcel> parcels = db.parcelDao().getAll();
        return parcels;
    }*/

    private static Parcel getParcel(final AppDatabase db, int serialNo) {
        Parcel parcel = db.parcelDao().findById(serialNo);
        return parcel;
    }

    private static int getCount(final AppDatabase db) {
        int count = db.parcelDao().countParcels();
        return count;
    }

    private static void deleteParcel(final AppDatabase db, Parcel parcel) {
        db.parcelDao().delete(parcel);

    }

    private static void deleteAllParcel(final AppDatabase db) {
        db.parcelDao().nukeTable();

    }

    private static void updateParcel(AppDatabase db, Parcel parcel) {
        db.parcelDao().updateParcel(parcel);
    }

    public static Single<Parcel> addParcel(final Parcel parcel, final AppDatabase appDatabase) {

        return Single.fromCallable(() -> {
            long rowId = appDatabase.parcelDao().insert(parcel);
            Timber.d("Parcel stored " + rowId);
            return parcel;
        }).subscribeOn(Schedulers.io());

    }

    public static void updateParcel(final Context context, Parcel parcel) {

        UpdateParcelAsync task = new UpdateParcelAsync(AppDatabase.getAppDatabase(context), parcel);


        task.execute();

    }

/*
    public static List<Parcel> getAllParcels(final Context context) {

        GetAllAsync task = new GetAllAsync(AppDatabase.getAppDatabase(context), context);

        Log.d(TAG, "parcels getAllParcels " + parcels);

        try {
            return task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

*/

    public static void boomTable(final Context context) {

        DeleteAllAsync task = new DeleteAllAsync(AppDatabase.getAppDatabase(context));
        task.execute();
    }

    public static void deleteCurrentParcel(@NonNull final AppDatabase db, Parcel parcel) {
        DeleteUserAsync task = new DeleteUserAsync(db, parcel);
        task.execute();
    }

    /* public static class GetAllAsync extends AsyncTask<Void, Void, List<Parcel>> {

		 private final AppDatabase mDb;

		 ProgressDialog dialog;
		 Context context;
		 private List<Parcel> parcels;

		 public GetAllAsync(AppDatabase db, Context context) {
			 mDb = db;
			 this.context = context;

		 }

		 @Override
		 protected void onPreExecute() {
			 dialog = new ProgressDialog(context);
			 dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			 dialog.setCancelable(false);
			 dialog.setMessage("Retrieving...");
			 dialog.show();
		 }

		 @Override
		 protected void onPostExecute(List<Parcel> parcels) {
			 //    listener.onResultsReceived(parcels);
			 super.onPostExecute(parcels);

			 if (dialog.isShowing()) {
				 dialog.dismiss();
			 }
		 }

		 @Override
		 protected List<Parcel> doInBackground(final Void... params) {
			 parcels = getAllParcels(mDb);
			 Log.d(TAG, "parcels asynctask " + getAllParcels(mDb));
			 return parcels;
		 }


	 }
 */
    private static class DeleteAllAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;


        DeleteAllAsync(AppDatabase db) {
            mDb = db;

        }

        @Override
        protected Void doInBackground(final Void... params) {
            deleteAllParcel(mDb);
            return null;
        }

    }

    private static class DeleteUserAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;
        private final Parcel parcel;

        DeleteUserAsync(AppDatabase db, Parcel parcel) {
            mDb = db;
            this.parcel = parcel;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            deleteParcel(mDb, parcel);
            return null;
        }

    }


    /*  private static class AddParcelAsync extends AsyncTask<Void, Void, Parcel> {

		  private final AppDatabase mDb;
		  private final Parcel parcel;

		  AddParcelAsync(AppDatabase db, Parcel parcel) {
			  mDb = db;
			  this.parcel = parcel;
		  }

		  @Override
		  protected Parcel doInBackground(final Void... params) {
			  addParcel(mDb, parcel);

			  return parcel;
		  }

	  }
  */
    private static class UpdateParcelAsync extends AsyncTask<Void, Void, Parcel> {

        private final AppDatabase mDb;
        private final Parcel parcel;

        UpdateParcelAsync(AppDatabase db, Parcel parcel) {

            //Make Changes here TODO
            this.mDb = db;
            this.parcel = parcel;
        }

        @Override
        protected Parcel doInBackground(final Void... params) {
            updateParcel(mDb, parcel);

            return parcel;
        }

    }


}
