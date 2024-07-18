package com.co.solia.emotional.plan.models.mappers;

import com.co.solia.emotional.plan.models.daos.PlanDao;
import com.co.solia.emotional.plan.models.dtos.rq.CreatePlanRqDto;
import com.co.solia.emotional.plan.models.dtos.rs.CreatePlanRsDto;
import com.co.solia.emotional.plan.models.dtos.rs.GetPlanRsDto;
import com.co.solia.emotional.plan.models.dtos.rs.UpdatePlanRsDto;
import com.co.solia.emotional.share.models.validators.Validator;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * class to map the all resources about plan.
 * @author luis.bolivar.
 */
@UtilityClass
@Slf4j
public class PlanMapper {

    /**
     * get from {@link CreatePlanRqDto} get a {@link PlanDao}.
     * @param rq to get the {@link PlanDao}.
     * @return {@link Optional} of {@link PlanDao}.
     */
    public static Optional<PlanDao> getDaoFromRq(final CreatePlanRqDto rq){
        return Optional.of(PlanDao.builder()
                        .id(UUID.randomUUID())
                        .name(rq.name())
                        .camsByMonth(rq.camsByMonth())
                        .refreshByCam(rq.refreshByCam())
                        .priceMonth(rq.priceMonth())
                        .priceYear(rq.priceYear())
                        .updated(System.currentTimeMillis())
                .build());
    }

    /**
     * get a {@link CreatePlanRsDto} from {@link PlanDao}.
     * @param dao to get a {@link CreatePlanRsDto}.
     * @return {@link Optional} of {@link CreatePlanRsDto}.
     */
    public static Optional<CreatePlanRsDto> getCreateRsFromDao(final PlanDao dao) {
        return Stream.of(dao)
                .filter(Objects::nonNull)
                .filter(d -> Validator.isValidId(d.getId()))
                .findFirst()
                .map(PlanMapper::getCreatePlanRsFromDao);
    }

    /**
     * get a {@link CreatePlanRsDto} from {@link PlanDao}.
     * @param dao to get a {@link CreatePlanRsDto}.
     * @return {@link CreatePlanRsDto}.
     */
    private static CreatePlanRsDto getCreatePlanRsFromDao(final PlanDao dao) {
        return CreatePlanRsDto.builder()
                .id(dao.getId())
                .name(dao.getName())
                .camsByMonth(dao.getCamsByMonth())
                .refreshByCam(dao.getRefreshByCam())
                .priceMonth(dao.getPriceMonth())
                .priceYear(dao.getPriceYear())
                .build();
    }

    /**
     * get a {@link GetPlanRsDto} from {@link PlanDao}.
     * @param dao to get a {@link GetPlanRsDto}.
     * @return {@link Optional} of {@link GetPlanRsDto}.
     */
    public static Optional<GetPlanRsDto> getGetRsFromDao(final PlanDao dao) {
        return Stream.of(dao)
                .filter(Objects::nonNull)
                .filter(d -> Validator.isValidId(d.getId()))
                .findFirst()
                .map(PlanMapper::getGetPlanRsFromDao);
    }

    /**
     * get a {@link List} of {@link GetPlanRsDto} from a {@link List} of {@link PlanDao}.
     * @param dao to get a {@link GetPlanRsDto}.
     * @return {@link Optional} of {@link GetPlanRsDto}.
     */
    public static Optional<List<GetPlanRsDto>> getGetListRsFromListDao(final List<PlanDao> dao) {
        return Stream.of(dao)
                .filter(Objects::nonNull)
                .filter(l -> !l.isEmpty())
                .findFirst()
                .map(PlanMapper::getGetListPlanRsFromListDao);
    }

    /**
     * get a {@link GetPlanRsDto} from {@link PlanDao}.
     * @param daos to get a {@link GetPlanRsDto}.
     * @return {@link GetPlanRsDto}.
     */
    private static List<GetPlanRsDto> getGetListPlanRsFromListDao(final List<PlanDao> daos) {
        return daos.parallelStream().map(PlanMapper::getGetPlanRsFromDao).toList();
    }

    /**
     * get a {@link GetPlanRsDto} from {@link PlanDao}.
     * @param dao to get a {@link GetPlanRsDto}.
     * @return {@link GetPlanRsDto}.
     */
    private static GetPlanRsDto getGetPlanRsFromDao(final PlanDao dao) {
        return GetPlanRsDto.builder()
                .id(dao.getId())
                .name(dao.getName())
                .camsByMonth(dao.getCamsByMonth())
                .refreshByCam(dao.getRefreshByCam())
                .priceMonth(dao.getPriceMonth())
                .priceYear(dao.getPriceYear())
                .build();
    }

    /**
     * get a {@link UpdatePlanRsDto} from a {@link PlanDao}
     * @param dao dao to get {@link UpdatePlanRsDto}.
     * @return {@link Optional} of {@link UpdatePlanRsDto}.
     */
    public static Optional<UpdatePlanRsDto> getUpdateRsFromDao(final PlanDao dao) {
        return Stream.of(dao)
                .filter(Objects::nonNull)
                .filter(d -> d.getId() != null)
                .findFirst()
                .map(PlanMapper::getUpdatePlanRsFromDao);
    }

    /**
     * get a {@link UpdatePlanRsDto} from a {@link PlanDao}
     * @param dao dao to get {@link UpdatePlanRsDto}.
     * @return {@link Optional} of {@link UpdatePlanRsDto}.
     */
    private static UpdatePlanRsDto getUpdatePlanRsFromDao(final PlanDao dao) {
        return UpdatePlanRsDto.builder()
                .id(dao.getId())
                .name(dao.getName())
                .camsByMonth(dao.getCamsByMonth())
                .refreshByCam(dao.getRefreshByCam())
                .priceMonth(dao.getPriceMonth())
                .priceYear(dao.getPriceYear())
                .build();
    }
}
