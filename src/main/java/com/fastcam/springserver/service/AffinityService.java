package com.fastcam.springserver.service;

import com.fastcam.springserver.entity.*;
import com.fastcam.springserver.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@Transactional
public class AffinityService {
    private final AffinityRepository affinities;
    private final MemberItemRepository memberItems;
    private final ItemRepository items;
    private final MemberRepository members;

    public AffinityService(AffinityRepository affinities, MemberItemRepository memberItems,
                           ItemRepository items, MemberRepository members) {
        this.affinities = affinities;
        this.memberItems = memberItems;
        this.items = items;
        this.members = members;
    }

    /** 친밀도 정보가 없는 신규 회원은 1레벨, 0경험치로 만들어 줍니다. */
    public Affinity getOrCreate(int userId) {
        requireMember(userId);
        return affinities.findByUserId(userId).orElseGet(() -> {
            Affinity affinity = new Affinity();
            affinity.setUserId(userId);
            affinity.setAffinityExp(0);
            affinity.setAffinityLevel(1);
            return affinities.save(affinity);
        });
    }

    @Transactional(readOnly = true)
    public Map<String, Object> myInfo(int userId) {
        Affinity affinity = affinities.findByUserId(userId).orElse(null);
        if (affinity == null) {
            // readOnly 메서드에서는 저장하지 않고 초기 표시값만 반환합니다.
            affinity = new Affinity();
            affinity.setUserId(userId);
            affinity.setAffinityExp(0);
            affinity.setAffinityLevel(1);
        }
        requireMember(userId);
        return makeInfo(affinity);
    }

    /** 아이템 수량 감소와 경험치 증가는 반드시 한 번에 처리합니다. */
    public Map<String, Object> useItem(int userId, int itemId) {
        MemberItem owned = memberItems.findByUserIdAndItemId(userId, itemId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "보유하지 않은 아이템입니다."));
        if (owned.getQuantity() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "아이템 수량이 부족합니다.");
        }
        Item item = items.findById(itemId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "아이템을 찾을 수 없습니다."));

        owned.setQuantity(owned.getQuantity() - 1);
        memberItems.save(owned);

        Affinity affinity = getOrCreate(userId);
        affinity.setAffinityExp(affinity.getAffinityExp() + item.getExpValue());
        affinity.setAffinityLevel(calculateLevel(affinity.getAffinityExp()));
        affinities.save(affinity);

        Map<String, Object> result = new HashMap<>(makeInfo(affinity));
        result.put("usedItemName", item.getItemName());
        result.put("addedExp", item.getExpValue());
        result.put("msg", "OK");
        return result;
    }

    /** 랭킹은 별도 테이블 없이 누적 경험치를 기준으로 매번 계산합니다. */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> ranking() {
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Member member : members.findAll()) {
            Affinity affinity = affinities.findByUserId(member.getUserid()).orElse(null);
            int exp = affinity == null ? 0 : affinity.getAffinityExp();
            int level = affinity == null ? 1 : affinity.getAffinityLevel();
            Map<String, Object> row = new HashMap<>();
            row.put("userId", member.getUserid());
            row.put("nickname", member.getNickname());
            row.put("savefilename", member.getSavefilename() == null ? "" : member.getSavefilename());
            row.put("exp", exp);
            row.put("level", level);
            row.put("levelName", levelName(level));
            rows.add(row);
        }
        rows.sort((a, b) -> Integer.compare((int) b.get("exp"), (int) a.get("exp")));
        for (int i = 0; i < rows.size(); i++) rows.get(i).put("rank", i + 1);
        return rows;
    }

    public String toneGuide(int userId) {
        int level = affinities.findByUserId(userId).map(Affinity::getAffinityLevel).orElse(1);
        return switch (level) {
            case 2 -> "따뜻하고 다정한 존댓말로 대화하세요.";
            case 3 -> "친한 친구처럼 편안하고 자연스러운 존댓말로 대화하세요.";
            case 4 -> "매우 가까운 친구처럼 적극적으로 공감하며 친근하게 대화하세요.";
            case 5 -> "오래 알고 지낸 소울메이트처럼 다정하고 친밀하게 대화하세요.";
            default -> "처음 만난 사용자이므로 정중하고 조심스러운 존댓말로 대화하세요.";
        };
    }

    private Map<String, Object> makeInfo(Affinity affinity) {
        Map<String, Object> data = new HashMap<>();
        data.put("level", affinity.getAffinityLevel());
        data.put("exp", affinity.getAffinityExp());
        data.put("nextLevelExp", nextLevelExp(affinity.getAffinityLevel()));
        data.put("levelName", levelName(affinity.getAffinityLevel()));
        data.put("items", memberItems.findAllByUserIdOrderByItemIdAsc(affinity.getUserId()).stream()
                .map(owned -> {
                    Item item = items.findById(owned.getItemId()).orElse(null);
                    Map<String, Object> itemData = new HashMap<>();
                    itemData.put("itemId", owned.getItemId());
                    itemData.put("quantity", owned.getQuantity());
                    itemData.put("itemName", item == null ? "삭제된 아이템" : item.getItemName());
                    itemData.put("itemImage", item == null ? "" : item.getItemImage());
                    itemData.put("expValue", item == null ? 0 : item.getExpValue());
                    return itemData;
                }).toList());
        return data;
    }

    private int calculateLevel(int exp) {
        if (exp >= 900) return 5;
        if (exp >= 500) return 4;
        if (exp >= 250) return 3;
        if (exp >= 100) return 2;
        return 1;
    }

    private int nextLevelExp(int level) {
        return switch (level) { case 1 -> 100; case 2 -> 250; case 3 -> 500; case 4 -> 900; default -> 900; };
    }

    private String levelName(int level) {
        return switch (level) { case 2 -> "조금 가까운 사이"; case 3 -> "친한 사이";
            case 4 -> "아주 친한 사이"; case 5 -> "소울메이트"; default -> "처음 만난 사이"; };
    }

    private Member requireMember(int userId) {
        Member member = members.findByUserid(userId);
        if (member == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다.");
        return member;
    }
}
