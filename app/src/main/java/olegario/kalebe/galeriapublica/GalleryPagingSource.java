package olegario.kalebe.galeriapublica;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.ListenableFuturePagingSource;
import androidx.paging.PagingSource;
import androidx.paging.PagingState;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

//acessa dados usando GalleryRepository para montar uma página de dados que será entregue para o Adapter do RecycleView
public class GalleryPagingSource extends ListenableFuturePagingSource<Integer, ImageData>{
    GalleryRepository galleryRepository;

    Integer initialLoadSize = 0;

    //usada para consultar os dados e montar as paginas de dados
    public GalleryPagingSource(GalleryRepository galleryRepository){
        this.galleryRepository = galleryRepository;
    }

    //carrega uma página do GalleryRepository e retorna encapsulado em um objeto ListenableFuture
    @NonNull
    @Override
    public ListenableFuture<PagingSource.LoadResult<Integer, ImageData>> loadFuture(@NonNull PagingSource.LoadParams<Integer> loadParams) {
        Integer nextPageNumber = loadParams.getKey();
        if (nextPageNumber == null){
            nextPageNumber = 1;
            initialLoadSize = loadParams.getLoadSize();
        }

        Integer offSet = 0;
        if (nextPageNumber == 2) {
            offSet = initialLoadSize;
        }
        else {
            offSet = ((nextPageNumber - 1) * loadParams.getLoadSize()) + (initialLoadSize - loadParams.getLoadSize());
        }

        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());

        Integer finalOffSet = offSet;
        Integer finalNextPageNumber = nextPageNumber;
        ListenableFuture<PagingSource.LoadResult<Integer, ImageData>> lf = service.submit(new Callable<PagingSource.LoadResult<Integer, ImageData>>() {
            @Override
            public PagingSource.LoadResult<Integer, ImageData> call() {
                List<ImageData> imageDataList = null;
                try {
                    imageDataList = galleryRepository.loadImageData(loadParams.getLoadSize(), finalOffSet);
                    Integer nextKey = null;
                    if (imageDataList.size() >= loadParams.getLoadSize()) {
                        nextKey = finalNextPageNumber + 1;
                    }
                    return new PagingSource.LoadResult.Page<Integer, ImageData>(imageDataList, null, nextKey);
                } catch (FileNotFoundException e) {
                    return new PagingSource.LoadResult.Error<>(e);
                }
            }
        });
        return lf;
    }

    //metodo padrao
    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, ImageData> pagingState) {
        return null;
    }
}