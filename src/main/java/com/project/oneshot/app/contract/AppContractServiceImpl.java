package com.project.oneshot.app.contract;

import com.project.oneshot.command.ClientVO;
import com.project.oneshot.command.ContractVO;
import com.project.oneshot.command.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class AppContractServiceImpl implements AppContractService {

    @Autowired
    AppContractMapper appContractMapper;

    @Override
    public List<ClientVO> getClientList() {
        return appContractMapper.getClientList();
    }

    @Override
    public List<ProductVO> getProductList() {
        return appContractMapper.getProductList();
    }

    @Override
    public List<ContractVO> getContractList(int clientNo, int productNo) {
        return appContractMapper.getContractList(clientNo, productNo);
    }

    @Override
    public void registerContract(ContractVO vo) {
        System.out.println("vo1 = " + vo);

        // 중복되는 계약을 확인하여, 신규 계약의 범위와 겹치는 기존 계약이 있는지 확인
        int overlappingCount = appContractMapper.countOverlappingContracts(
                vo.getProductNo(),
                vo.getClientNo(),
                vo.getContractSdate(),
                vo.getContractEdate(),
                vo.getContractPriceStatus()
        );
        System.out.println("overlappingCount = " + overlappingCount);

        // 중복되는 계약이 있는 경우 처리
        if (overlappingCount > 0) {
            // 기존 계약의 수정이 필요한 상황 처리
            List<ContractVO> overlappingContracts = appContractMapper.getOverlappingContracts(
                    vo.getProductNo(),
                    vo.getClientNo(),
                    vo.getContractSdate(),
                    vo.getContractEdate(),
                    vo.getContractPriceStatus()
            );

            // 신규 계약의 시작일과 종료일을 LocalDate로 변환
            LocalDate newContractStartDate = vo.getContractSdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate newContractEndDate = vo.getContractEdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            for (ContractVO existingContract : overlappingContracts) {
                LocalDate existingStartDate = existingContract.getContractSdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate existingEndDate = existingContract.getContractEdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                // 케이스 4: 신규 계약이 기존 계약을 완전히 포함하는 경우
                if (newContractStartDate.isBefore(existingStartDate) && newContractEndDate.isAfter(existingEndDate) || (newContractStartDate.isEqual(existingStartDate) && newContractEndDate.isEqual(existingEndDate))) {
                    // 기존 계약의 시작일과 종료일을 신규 계약의 시작일과 종료일로 업데이트
                    existingContract.setContractPrice(vo.getContractPrice());
                    existingContract.setContractSdate(vo.getContractSdate());
                    existingContract.setContractEdate(vo.getContractEdate());
                    appContractMapper.deleteContract(existingContract);
                } else {
                    // 케이스 3: 신규 계약이 기존 계약의 중간에 있는 경우 -> 기존 계약을 분할
                    if (newContractStartDate.isAfter(existingStartDate) && newContractEndDate.isBefore(existingEndDate)) {
                        // 기존 계약의 원래 종료일을 변수에 저장
                        Date originalEndDate = existingContract.getContractEdate();
                        System.out.println("Original End Date: " + originalEndDate);

                        // 기존 계약의 첫 번째 부분 종료일 설정
                        LocalDate firstPartEndDate = newContractStartDate.minusDays(1);
                        existingContract.setContractEdate(Date.from(firstPartEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                        appContractMapper.updateContract(existingContract);
                    } else {
                        // 케이스 1: 기존 계약의 종료일이 신규 계약의 종료일보다 늦으면
                        if (newContractEndDate.isBefore(existingEndDate)) {
                            System.out.println("case1");
                            LocalDate newExistingStartDate = newContractEndDate.plusDays(1);
                            existingContract.setContractSdate(Date.from(newExistingStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                            appContractMapper.updateContract(existingContract);
                        }

                        // 케이스 2: 신규 계약이 기존 계약의 중간에 겹치는 경우, 기존 계약의 종료일을 조정
                        if (newContractStartDate.isAfter(existingStartDate) && newContractStartDate.isBefore(existingEndDate)) {
                            System.out.println("case2");
                            LocalDate newExistingEndDate = newContractStartDate.minusDays(1);
                            existingContract.setContractEdate(Date.from(newExistingEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                            appContractMapper.updateContract(existingContract);
                        }
                    }
                }
            }
        }

        // 신규 계약 등록
        appContractMapper.getRegist(vo);
    }
}