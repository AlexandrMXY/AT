package ru.mephi.bakinaa.regex.nfa;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public abstract class NFATransition {
    private final int target;

    public NFATransition(int target) {
        this.target = target;
    }

    public abstract boolean move(StringHolder stringHolder, CaptureBuffer captureBuffer);
    public abstract void backstep(StringHolder stringHolder, CaptureBuffer captureBuffer);

    @Getter
    public static class StandardTransition extends NFATransition {
        private final int charId;

        public StandardTransition(int target, int charId) {
            super(target);
            this.charId = charId;
        }

        @Override
        public boolean move(StringHolder stringHolder, CaptureBuffer captureBuffer) {
            boolean canMove =  stringHolder.popIf(charId);
            if (canMove)
                captureBuffer.nextChar();
            return canMove;
        }

        @Override
        public void backstep(StringHolder stringHolder, CaptureBuffer captureBuffer) {
            captureBuffer.backstep();
            stringHolder.backstep();
        }
    }

    @Getter
    public static class EpsilonTransition extends NFATransition {
        public EpsilonTransition(int target) {
            super(target);
        }

        @Override
        public boolean move(StringHolder stringHolder, CaptureBuffer captureBuffer) {
            return true;
        }

        @Override
        public void backstep(StringHolder stringHolder, CaptureBuffer captureBuffer) {}
    }

    @Getter
    public static class CaptureBeginTransition extends NFATransition {
        private final int groupId;

        public CaptureBeginTransition(int target, int group) {
            super(target);
            groupId = group;
        }

        @Override
        public boolean move(StringHolder stringHolder, CaptureBuffer captureBuffer) {
            captureBuffer.beginCapterue(groupId, stringHolder.getIndex());
            return true;
        }

        @Override
        public void backstep(StringHolder stringHolder, CaptureBuffer captureBuffer) {
            captureBuffer.endCapture(groupId);
        }
    }

    @Getter
    public static class CaptureEndTransition extends NFATransition {
        private final int groupId;

        public CaptureEndTransition(int target, int group) {
            super(target);
            groupId = group;
        }

        @Override
        public boolean move(StringHolder stringHolder, CaptureBuffer captureBuffer) {
            captureBuffer.endCapture(groupId);
            return true;
        }

        @Override
        public void backstep(StringHolder stringHolder, CaptureBuffer captureBuffer) {
            captureBuffer.continueCapture(groupId);
        }
    }

    @Getter
    public static class BackreferenceTransition extends NFATransition {
        private final int groupId;

        public BackreferenceTransition(int target, int group) {
            super(target);
            groupId = group;
        }

        @Override
        public boolean move(StringHolder stringHolder, CaptureBuffer captureBuffer) {
            CaptureBuffer.GroupInfo captured = captureBuffer.getCaptured(groupId);
            boolean consumed = stringHolder.tryConsume(captured);
            if (consumed)
                captureBuffer.nextChars(captured.len());
            return consumed;
        }

        @Override
        public void backstep(StringHolder stringHolder, CaptureBuffer captureBuffer) {
            CaptureBuffer.GroupInfo captured = captureBuffer.getCaptured(groupId);
            stringHolder.backstep(captured);
            captureBuffer.backstep(captured.len());
        }
    }
}
