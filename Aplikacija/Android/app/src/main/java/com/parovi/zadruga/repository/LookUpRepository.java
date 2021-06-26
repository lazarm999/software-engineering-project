package com.parovi.zadruga.repository;

import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.factories.ApiFactory;
import com.parovi.zadruga.factories.DaoFactory;
import com.parovi.zadruga.models.entityModels.Badge;
import com.parovi.zadruga.models.entityModels.Faculty;
import com.parovi.zadruga.models.entityModels.Location;
import com.parovi.zadruga.models.entityModels.Tag;
import com.parovi.zadruga.models.entityModels.University;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LookUpRepository extends BaseRepository {
    public LookUpRepository(){super();}

    public void getAllLocations(String token, MutableLiveData<CustomResponse<?>> locations){
        ApiFactory.getLookupApi().getAllLocations(token).enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(@NotNull Call<List<Location>> call, @NotNull Response<List<Location>> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(locations != null){
                        locations.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                    }
                    Utility.getExecutorService().execute(new Runnable() {
                        @Override
                        public void run() {
                            DaoFactory.getLocationDao().insertOrUpdate(response.body());
                        }
                    });
                } else
                    responseNotSuccessful(response.code(), locations);
            }

            @Override
            public void onFailure(@NotNull Call<List<Location>> call, @NotNull Throwable t) {
                apiCallOnFailure(t.getMessage(), locations);
            }
        });
    }

    public void getAllBadges(String token, MutableLiveData<CustomResponse<?>> badges){
        ApiFactory.getLookupApi().getAllBadges(token).enqueue(new Callback<List<Badge>>() {
            @Override
            public void onResponse(@NotNull Call<List<Badge>> call, @NotNull Response<List<Badge>> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(badges != null){
                        badges.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                    }
                    Utility.getExecutorService().execute(new Runnable() {
                        @Override
                        public void run() {
                            DaoFactory.getBadgeDao().insertOrUpdate(response.body());
                        }
                    });
                } else
                    responseNotSuccessful(response.code(), badges);
            }

            @Override
            public void onFailure(@NotNull Call<List<Badge>> call, @NotNull Throwable t) {
                apiCallOnFailure(t.getMessage(), badges);
            }
        });
    }

    public void getAllTags(String token, MutableLiveData<CustomResponse<?>> tags){
        ApiFactory.getLookupApi().getAllTags(token).enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(@NotNull Call<List<Tag>> call, @NotNull Response<List<Tag>> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(tags != null){ //ako je null onda samo hocemo da napunimo lokalnu bazu, a ko nije null onda hocemo da napunimo i liveData-u
                        tags.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                    }
                    Utility.getExecutorService().execute(new Runnable() {
                        @Override
                        public void run() {
                            DaoFactory.getTagDao().insertOrUpdate(response.body());
                        }
                    });
                } else
                    responseNotSuccessful(response.code(), tags);
            }

            @Override
            public void onFailure(@NotNull Call<List<Tag>> call, @NotNull Throwable t) {
                apiCallOnFailure(t.getMessage(), tags);
            }
        });
    }

    //ovo mislim da nece nikad ni da nam treba
    public void getFacultiesAndUniversities(String token){
        ApiFactory.getLookupApi().getAllUniversities(token).enqueue(new Callback<List<University>>() {
            @Override
            public void onResponse(@NotNull Call<List<University>> call, @NotNull Response<List<University>> universitiesRes) {
                if(universitiesRes.isSuccessful() && universitiesRes.body() != null){
                    ApiFactory.getLookupApi().getAllFaculties(token).enqueue(new Callback<List<Faculty>>() {
                        @Override
                        public void onResponse(@NotNull Call<List<Faculty>> call, @NotNull Response<List<Faculty>> response) {
                            if(response.isSuccessful() && response.body() != null){
                                List<Faculty> tmpFaculties = response.body();
                                for (Faculty f: tmpFaculties) {
                                    f.setFkUniversityId(f.getUniversity().getUniversityId());
                                }
                                Utility.getExecutorService().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        DaoFactory.getUniversityDao().insertOrUpdate(universitiesRes.body());
                                        DaoFactory.getFacultyDao().insertOrUpdate(tmpFaculties);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<List<Faculty>> call, @NotNull Throwable t) {
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<University>> call, @NotNull Throwable t) {
            }
        });
    }

    public void getAllFaculties(String token, MutableLiveData<CustomResponse<?>> faculties){
        ApiFactory.getLookupApi().getAllFaculties(token).enqueue(new Callback<List<Faculty>>() {
            @Override
            public void onResponse(@NotNull Call<List<Faculty>> call, @NotNull Response<List<Faculty>> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(faculties != null){
                        faculties.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                    }
                    List<Faculty> tmpFaculties = response.body();
                    for (Faculty f: tmpFaculties) {
                        f.setFkUniversityId(f.getUniversity().getUniversityId());
                    }
                    Utility.getExecutorService().execute(new Runnable() {
                        @Override
                        public void run() {
                            //DaoFactory.getFacultyDao().insertOrUpdate(tmpFaculties);
                        }
                    });
                } else
                    responseNotSuccessful(response.code(), faculties);
            }

            @Override
            public void onFailure(@NotNull Call<List<Faculty>> call, @NotNull Throwable t) {
                apiCallOnFailure(t.getMessage(), faculties);
            }
        });
    }

    public void getAllUniversities(String token, MutableLiveData<CustomResponse<?>> universities){
        ApiFactory.getLookupApi().getAllUniversities(token).enqueue(new Callback<List<University>>() {
            @Override
            public void onResponse(@NotNull Call<List<University>> call, @NotNull Response<List<University>> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(universities != null){
                        universities.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                    }
                    Utility.getExecutorService().execute(new Runnable() {
                        @Override
                        public void run() {
                            DaoFactory.getUniversityDao().insertOrUpdate(response.body());
                        }
                    });
                } else
                    responseNotSuccessful(response.code(), universities);
            }

            @Override
            public void onFailure(@NotNull Call<List<University>> call, @NotNull Throwable t) {
                apiCallOnFailure(t.getMessage(), universities);
            }
        });
    }
}
