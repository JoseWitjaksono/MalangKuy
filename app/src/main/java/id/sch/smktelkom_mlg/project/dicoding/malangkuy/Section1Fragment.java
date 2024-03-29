package id.sch.smktelkom_mlg.project.dicoding.malangkuy;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.ProgressDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Section1Fragment extends Fragment {
	//recyclerview object
	private RecyclerView recyclerView;
	private LinearLayoutManager mLayoutManager;
	//adapter object
	private RecyclerView.Adapter adapter;

	//database reference
	private DatabaseReference mDatabase;

	//progress dialog
	private ProgressDialog progressDialog;

	//list to hold all the uploaded images
	private List<Upload> uploads;

	public Section1Fragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View myView = inflater.inflate(R.layout.fragment_section1, container, false);


		recyclerView = (RecyclerView) myView.findViewById(R.id.recyclerView);
		recyclerView.setHasFixedSize(true);
		mLayoutManager = new LinearLayoutManager(getActivity());
		mLayoutManager.setReverseLayout(true);
		mLayoutManager.setStackFromEnd(true);
		recyclerView.setLayoutManager(mLayoutManager);


		progressDialog = new ProgressDialog(getActivity());

		uploads = new ArrayList<>();
		final Context mContext = getActivity().getApplicationContext();

		//displaying progress dialog while fetching images
		progressDialog.setMessage("Please wait...");
		progressDialog.show();
		mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

		//adding an event listener to fetch values
		mDatabase.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				//dismissing the progress dialog
				progressDialog.dismiss();


				//iterating through all the values in database
				for (DataSnapshot postSnapshot : snapshot.getChildren()) {
					Upload upload = postSnapshot.getValue(Upload.class);
					uploads.add(upload);
				}
				//creating adapter
				adapter = new MyAdapter(mContext, uploads);

				//adding adapter to recyclerview
				recyclerView.setAdapter(adapter);
			}
			@Override
			public void onCancelled(DatabaseError databaseError) {
				progressDialog.dismiss();
			}
		});
		return myView;
	}
}