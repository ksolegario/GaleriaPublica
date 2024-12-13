package olegario.kalebe.galeriapublica;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GridViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GridViewFragment extends Fragment {

    private MainViewModel mViewModel;
    private View view;

    ///cria uma instância do Fragmento e retorna
    public static GridViewFragment newInstance() {
        return new GridViewFragment();
    }

    //chamado toda vez que o fragmento é setado em uma Activity, criar a interface de usuário do fragmento
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_grid_view, container, false);
        return view;
    }

    //chamado depois de onCreateView
    //Quando chamado garante-se que todos os elementos da interface do fragmento foram criados.
    //dentro deste método que se configura o RecycleView.
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        GridAdapter gridAdapter = new GridAdapter(new ImageDataComparator());
        LiveData<PagingData<ImageData>> liveData = mViewModel.getPageLv();
        liveData.observe(getViewLifecycleOwner(), new Observer<PagingData<ImageData>>() {

            @Override
            public void onChanged(PagingData<ImageData> objectPagingData) {
                gridAdapter.submitData(getViewLifecycleOwner().getLifecycle(),objectPagingData);
            }
        });

        RecyclerView rvGallery = (RecyclerView) view.findViewById(R.id.rvGrid);
        rvGallery.setAdapter(gridAdapter);
        rvGallery.setLayoutManager(new GridLayoutManager(getContext(), 4));
    }
}